/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *  
 *   http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *  
 *******************************************************************************/
 

package org.apache.wink.common.internal.registry.metadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.wink.common.DynamicResource;
import org.apache.wink.common.annotations.Parent;
import org.apache.wink.common.annotations.Workspace;
import org.apache.wink.common.internal.registry.Injectable;
import org.apache.wink.common.internal.registry.InjectableFactory;


/**
 * Collects ClassMetadata from JAX-RS Resource classes
 */
public class ResourceMetadataCollector extends AbstractMetadataCollector {
    
    private static final Logger logger = LoggerFactory.getLogger(ResourceMetadataCollector.class);

    private ResourceMetadataCollector(Class<?> clazz) {
        super(clazz);
    }

    public static boolean isResource(Class<?> cls) {
        return (isStaticResource(cls) || isDynamicResource(cls));
    }

    public static boolean isStaticResource(Class<?> cls) {
        return cls.getAnnotation(Path.class) != null;
    }

    public static boolean isDynamicResource(Class<?> cls) {
        return DynamicResource.class.isAssignableFrom(cls);
    }

    public static ClassMetadata collectMetadata(Class<?> clazz) {
        ResourceMetadataCollector collector = new ResourceMetadataCollector(clazz);
        collector.parseClass();
        collector.parseFields();
        collector.parseConstructors();
        collector.parseMethods();
        return collector.getMetadata();
    }

    @Override
    protected final Injectable parseAccessibleObject(AccessibleObject field, Type fieldType) {
        Injectable injectable = InjectableFactory.getInstance().create(fieldType, field.getAnnotations(), (Member) field);
        if (injectable.getParamType() == Injectable.ParamType.ENTITY) {
            // EntityParam should be ignored for fields (see JSR-311 3.2)
            return null;
        }
        return injectable;
    }

    private void parseClass() {
        Class<?> cls = getMetadata().getResourceClass();
        parseClass(cls);
    }

    private boolean parseClass(Class<?> cls) {

        boolean workspacePresent = parseWorkspace(cls);
        boolean pathPresent = parsePath(cls);
        boolean consumesPresent = parseClassConsumes(cls);
        boolean producesPresent = parseClassProduces(cls);

        Parent parent = cls.getAnnotation(Parent.class);
        if (parent != null) {
            getMetadata().getParents().add(parent.value());
        }

        // if the class contained any annotations, we can to stop
        if (workspacePresent || pathPresent || consumesPresent || producesPresent) {
            return true;
        }

        //        // Do we want to allow searching for class annotations in superclass??? 
        //       
        //        // parse superclass
        //        Class<?> superclass = cls.getSuperclass();
        //        if (superclass != null) {
        //            return parseClass(superclass);
        //        }
        //         
        //        // parse all interfaces
        //        Class<?>[] interfaces = cls.getInterfaces();
        //        for (Class<?> interfaceClass : interfaces) {
        //            // stop with the first interface that has annotations
        //            if (parseClass(interfaceClass)) {
        //                return true;
        //            }
        //        }

        // no annotations
        return false;
    }

    private boolean parseWorkspace(Class<?> cls) {
        Workspace workspace = cls.getAnnotation(Workspace.class);
        if (workspace != null) {
            getMetadata().setWorkspaceName(workspace.workspaceTitle());
            getMetadata().setCollectionTitle(workspace.collectionTitle());
            return true;
        }
        return false;
    }

    private boolean parsePath(Class<?> cls) {
        Path path = cls.getAnnotation(Path.class);
        if (path != null) {
            getMetadata().addPath(path.value());
            return true;
        }
        return false;
    }

    private void parseMethods() {
        F1: for (Method method : getMetadata().getResourceClass().getMethods()) {
            Class<?> declaringClass = method.getDeclaringClass();
            if (method.getDeclaringClass() == Object.class) {
                continue F1;
            }
            MethodMetadata methodMetadata = createMethodMetadata(method);
            if (methodMetadata != null) {
                String path = methodMetadata.getPath();
                // sub-resource
                if (path != null) {
                    Set<String> httpMethods = methodMetadata.getHttpMethod();
                    if (!httpMethods.isEmpty()) {
                        // sub-resource method
                        getMetadata().getSubResourceMethods().add(methodMetadata);
                    } else {
                        // sub-resource locator
                        // verify that the method does not take an entity parameter
                        String methodName = String.format("%s.%s", declaringClass.getName(), method.getName());
                        for (Injectable id : methodMetadata.getFormalParameters()) {
                            if (id.getParamType() == Injectable.ParamType.ENTITY) {
                                logger.warn(String.format("Sub-Resource locator %s contains an illegal entity parameter. The locator will be ignored.", methodName));
                                continue F1;
                            }
                        }
                        // log a warning if the locator has a Produces or Consumes annotation
                        if (!methodMetadata.getConsumes().isEmpty() || !methodMetadata.getProduces().isEmpty()) {
                            logger.warn(String.format("Sub-Resource locator %s is annotated with Consumes/Produces. These annotations are ignored for sub-resource locators", methodName));
                        }
                        getMetadata().getSubResourceLocators().add(methodMetadata);
                    }
                } else {
                    // resource method
                    getMetadata().getResourceMethods().add(methodMetadata);
                }
            }
        }
    }

    private MethodMetadata createMethodMetadata(Method method) {

        int modifiers = method.getModifiers();
        // only public, non-static methods
        if (Modifier.isStatic(modifiers) || !Modifier.isPublic(modifiers)) {
            return null;
        }

        boolean hasAnnotation = false;
        HttpMethod httpMethod = getHttpMethod(method);
        Path path = getPath(method);

        MethodMetadata metadata = new MethodMetadata(getMetadata());
        metadata.setReflectionMethod(method);
        if (path != null) {
            hasAnnotation = true;
            metadata.addPath(path.value());
        }
        if (httpMethod != null) {
            hasAnnotation = true;
            metadata.getHttpMethod().add(httpMethod.value());
        }

        String[] consumes = getConsumes(method);
        for (String mediaType : consumes) {
            hasAnnotation = true;
            metadata.addConsumes(MediaType.valueOf(mediaType));
        }

        String[] produces = getProduces(method);
        for (String mediaType : produces) {
            hasAnnotation = true;
            metadata.addProduces(MediaType.valueOf(mediaType));
        }

        // if the method has not annotation at all,
        // then it may override a method in a superclass or interface that has annotations, 
        // so try looking at the overridden method annotations
        if (!hasAnnotation) {

            Class<?> declaringClass = method.getDeclaringClass();

            // try a superclass
            Class<?> superclass = declaringClass.getSuperclass();
            if (superclass != null && superclass != Object.class) {
                MethodMetadata createdMetadata = createMethodMetadata(superclass, method);
                // stop with if the method found
                if (createdMetadata != null) {
                    return createdMetadata;
                }
            }

            // try interfaces
            Class<?>[] interfaces = declaringClass.getInterfaces();
            for (Class<?> interfaceClass : interfaces) {
                MethodMetadata createdMetadata = createMethodMetadata(interfaceClass, method);
                // stop with the first method found
                if (createdMetadata != null) {
                    return createdMetadata;
                }
            }

            return null;
        }

        parseMethodParameters(method, metadata);

        return metadata;
    }

    private MethodMetadata createMethodMetadata(Class<?> declaringClass, Method method) {
        try {
            Method declaredMethod = declaringClass.getDeclaredMethod(method.getName(),
                method.getParameterTypes());
            return createMethodMetadata(declaredMethod);
        } catch (SecurityException e) {
            // can't get to overriding method
            return null;
        } catch (NoSuchMethodException e) {
            // no overriding method exists
            return null;
        }
    }

    private boolean parseClassConsumes(Class<?> cls) {
        String[] consumes = getConsumes(cls);
        //        if (consumes.length == 0) {
        //            getMetadata().addConsumes(MediaType.WILDCARD_TYPE);
        //            return false;
        //        }
        for (String mediaType : consumes) {
            getMetadata().addConsumes(MediaType.valueOf(mediaType));
        }
        return true;
    }

    private boolean parseClassProduces(Class<?> cls) {
        String[] consumes = getProduces(cls);
        //        if (consumes.length == 0) {
        //            getMetadata().addProduces(MediaType.WILDCARD_TYPE);
        //            return false;
        //        }
        for (String mediaType : consumes) {
            getMetadata().addProduces(MediaType.valueOf(mediaType));
        }
        return true;
    }

    private String[] getConsumes(AnnotatedElement element) {
        Consumes consumes = element.getAnnotation(Consumes.class);
        if (consumes != null) {
            return consumes.value();
        }
        return new String[] {};
    }

    private String[] getProduces(AnnotatedElement element) {
        Produces produces = element.getAnnotation(Produces.class);
        if (produces != null) {
            return produces.value();
        }
        return new String[] {};
    }

    private Path getPath(Method method) {
        return method.getAnnotation(Path.class);
    }

    private HttpMethod getHttpMethod(Method method) {
        HttpMethod httpMethod = method.getAnnotation(HttpMethod.class);
        if (httpMethod != null) {
            return httpMethod;
        }

        // search if any of the annotations is annotated with HttpMethod
        // such as @GET
        for (Annotation annotation : method.getAnnotations()) {
            httpMethod = annotation.annotationType().getAnnotation(HttpMethod.class);
            if (httpMethod != null) {
                return httpMethod;
            }
        }
        return null;
    }

    private void parseMethodParameters(Method method, MethodMetadata metadata) {
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Type[] paramTypes = method.getGenericParameterTypes();
        boolean entityParamExists = false;
        for (int pos = 0, limit = paramTypes.length; pos < limit; pos++) {
            Injectable fp = InjectableFactory.getInstance().create(paramTypes[pos], parameterAnnotations[pos],
                method);
            if (fp.getParamType() == Injectable.ParamType.ENTITY) {
                if (entityParamExists) {
                    // we are allowed to have only one entity parameter
                    String methodName = method.getDeclaringClass().getName() + "."+ method.getName();
                    throw new IllegalStateException("Resource method " + methodName + " has more than one entity parameter");
                }
                entityParamExists = true;
            }
            metadata.getFormalParameters().add(fp);
        }
    }

    @Override
    protected final boolean isConstructorParameterValid(Injectable fp) {
        // This method is declared as final, since parseConstructors(), which calls it, is invoked from the constructor
        return !(fp.getParamType() == Injectable.ParamType.ENTITY);
    }

}