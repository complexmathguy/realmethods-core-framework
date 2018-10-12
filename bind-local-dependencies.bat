call mvn install:install-file -Dfile=.\dependencies\mq\mq.jar -DgroupId=com.ibm -DartifactId=com.ibm.mq -Dversion=1.0.0 -Dpackaging=jar
call mvn install:install-file -Dfile=.\dependencies\mq\mqbind.jar -DgroupId=com.ibm -DartifactId=com.ibm.mq.bind -Dversion=1.0.0 -Dpackaging=jar
call mvn install:install-file -Dfile=.\dependencies\oracle\classes12.zip -DgroupId=com.oracle -DartifactId=com.oracle.jdbc -Dversion=1.0.0 -Dpackaging=jar
