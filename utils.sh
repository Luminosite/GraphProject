#!/bin/bash

function getVersion(){
    version=`grep '^version' build.sbt | sed -e 's/.*"\(.*\)".*/\1/'`
    echo ${version}
}
    
