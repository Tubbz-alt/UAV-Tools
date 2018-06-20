#!/bin/bash
#Author: Jesimar da Silva Arantes
#Date: 02/06/2016
#Last Update: 19/02/2018
#Description: Run a set of simulations in the flightgear
#Descrição: Executa uma conjunto de simulações no flightgear

echo "========================Iniciando Simulacoes========================"
for i in {0..100};
do
	echo "--------Iniciando Simulacao $i--------"
	cd 1aps-model-flightgear/
	echo "<<<Iniciando Model FlighGear>>>"
	dd
	sleep 4
	cd ..
	cd 2aps-control-target/
	echo "<<<Iniciando Control Target>>>"
	java -jar aps-control-target-1.0.jar > out/output$i.txt &
	sleep 4
	cd ..
	cd 3aps-adapter-route/
	echo "<<<Iniciando Adapter Route>>>"
	java -jar aps-adapter-route-1.0.jar > out/output$i.txt &
	sleep 4
	cd ..
	cd 4aps-plotter-aircraft/
	echo "<<<Iniciando Plotter Aircraft>>>"
	java -jar aps-plotter-aircraft-1.0.jar > out/output$i.txt &
	sleep 4
	cd ..
	cd 5aps-model-flightgear/
	echo "<<<Iniciando Model FlightGear>>>\n"
	java -jar aps-model-flightgear-1.0.jar > out/output$i.txt &
	sleep 4
	cd ..
	echo "--------Aguardando Simulacao--------"
	sleep 124
	echo "--------Matando Processos--------"
	killall -9 fgfs
	sleep 1
	killall -9 java
	sleep 1
	echo "--------Copiando Resultados--------"
	mv 2aps-control-target/output/ results/sim$i/	
	echo "--------Aguardando Proxima Simulacao--------"
	sleep 30
done
