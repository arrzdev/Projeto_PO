#!/bin/bash
export CLASSPATH=$(pwd)/po-uilib/po-uilib.jar:$(pwd)/prr-core/prr-core.jar:$(pwd)/prr-app/prr-app.jar
for x in tests/*.in; do
    if [ -e ${x%.in}.import ]; then
        java -Dimport=${x%.in}.import -Din=$x -Dout=${x%.in}.outhyp prr.app.App;
    else
        java -Din=$x -Dout=${x%.in}.outhyp prr.app.App;
    fi

    diff -cB -w ${x%.in}.out ${x%.in}.outhyp > ${x%.in}.diff ;
    if [ -s ${x%.in}.diff ]; then
        echo "FAIL: $x. See file ${x%.in}.diff " ;
    else
        echo -n "."
        rm -f ${x%.in}.diff ${x%.in}.outhyp ; 
    fi
done

#rm -f saved*

echo "Done."

