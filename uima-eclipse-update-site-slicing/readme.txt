Use this maven project (maven build with a test target) to 
take a UIMA subsite and "slice off" just the lastest version of the feature groups.

Source: the original in dist.apache.org's svn for release of a UIMA Eclipse plugin subsite.
Destination: this project's target/eclipse-update-site/<subsite-name>

Subsites are for 
  - UIMAJ SDK   uimaj, and uimaj-v3-pre-production
  - RUTA        ruta
  - UIMA-AS     uima-as
  
The slicing operation needs to regenerate the gpg signatures, so it will ask for your signing key.
This is only done if you include the apache-release profile.  
  
  
How to run
----------

1) update the subsiteSlicing.xml to specify the slicing details - names of the features, 
   expressed as installable unit names - you need to format these like the example there
   ending in .feature.group.

2) mvn test -Dsubsite=xxxxx  -Papache-release 
  - change property subsite to "uimaj", "ruta", "uimaj-v3-pre-production", etc.

