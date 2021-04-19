#!/bin/bash
cd core/buildSrc
rm -r build
gradle

cd ../..

cd monticore/buildSrc
rm -r build
gradle

cd ../..

cd runtime/buildSrc
rm -r build
gradle
