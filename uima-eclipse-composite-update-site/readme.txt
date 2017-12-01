How to maintain the UIMA Eclipse update site

The Eclipse update site is a composite update site.  The top level has subsites for
  - uimaj sdk
  - ruta
  - uima-as
  
The composite update site only needs maintenance if the number of subsites changes.  Normally this is not the case.
See the uima-eclipse-composite-update-site project's "buildCompositeRepository.xml" for details.