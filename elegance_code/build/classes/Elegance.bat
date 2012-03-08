javac *.java
jar cvf Elegance.jar *.class
java -classpath mysql-connector-java-5.0.5-bin.jar;Elegance.jar -Xmx1024m -Xss16m Elegance
