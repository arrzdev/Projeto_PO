#!/bin/bash

right=0
left=0

for x in tests/*.in; do
    if [ -e ${x%.in}.import ]; then
        java -Dimport=${x%.in}.import -Din=$x -Dout=${x%.in}.outhyp prr.app.App;
    else
        java -Din=$x -Dout=${x%.in}.outhyp prr.app.App;
    fi

    diff -cB -w ${x%.in}.out ${x%.in}.outhyp > ${x%.in}.diff ;
    if [ -s ${x%.in}.diff ]; then
        echo "FAIL: $x. See file ${x%.in}.diff " ;
        left=$((left + 1));
    else
        echo "PASSED: $x"
        right=$((right + 1))
        rm -f ${x%.in}.diff ${x%.in}.outhyp ; 

    fi
done

#rm -f saved*

echo "SCORE:" $right"/"$((right + left))
echo "PASSED: $right"
echo "FAILED: $left"
echo "Done."