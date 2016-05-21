#usage:  ./q.sh q1.rq

val=$(python -c "from urllib import urlencode; print urlencode({'query':open('$1').read()})")
#$(../apache-jena-2.11.1/bin/rsparql --service http://127.0.0.1:3030/ds/sparql --query $val )


wget -qO- http://swipe.unica.it/jena/sparql?$val
