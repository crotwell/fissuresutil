#!/bin/sh

MAVEN=~/.maven/repository


SEEDCODEC=$MAVEN/SeedCodec/jars/SeedCodec-1.0Beta.jar
FISSURESUTIL=$MAVEN/fissuresUtil/jars/fissuresUtil-1.0.6beta.jar
FISSURESIMPL=$MAVEN/fissuresImpl/jars/fissuresImpl-1.1.4beta.jar
FISSURESIDL=$MAVEN/fissuresIDL/jars/fissuresIDL-1.0.jar
LOG4J=$MAVEN/log4j/jars/log4j-1.2.8.jar

JARS=${SEEDCODEC}:${FISSURESIDL}:${FISSURESIMPL}:${FISSURESUTIL}:${LOG4J}

java -cp ${JARS}:${CLASSPATH} edu.sc.seis.fissuresUtil.simple.SimpleNetworkClient -props ./simpleClient.prop

java -cp ${JARS}:${CLASSPATH} edu.sc.seis.fissuresUtil.simple.SimpleEventClient -props ./simpleClient.prop


java -cp ${JARS}:${CLASSPATH} edu.sc.seis.fissuresUtil.simple.SimpleSeismogramClient -props ./simpleClient.prop

echo done.

