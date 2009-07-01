
rem *** This batch file generates JAXB classes from the listed xsd files
rem *** IMPORTANT - the generated files most likely need to undergo code changes after generation

rem OpenSearch
xjc -nv -d OpenSearch1.1 -p org.apache.wink.common.model.opensearch openSearch1_1.xsd

rem Atom Syndication Format
xjc -nv -d AtomSyndicationFormat -p org.apache.wink.common.model.atom asf.xsd