# Modules-IFA

Este diretório é responsável por agrupar um conjunto de projetos usados pelo sistema *In-Flight Awareness* (IFA). 

Os seguintes projetos estão sendo utilizados pelo IFA.

* **Alarm-PC** -> Aplicação em Java para acionar o Alarme (Buzzer) do PC. Usado quando o sistema IFA executa localmente no PC ao invés do CC. Simula o hardware do atuador buzzer. [[Alarm-PC](./Alarm-PC/)]
* **Sonar-PC** -> Aplicação em Java para acionar o sensor sonar do PC. Usado quando o sistema IFA executa localmente no PC ao invés do CC. Simula o hardware do sensor sonar. [[Sonar-PC](./Sonar-PC/)]
* **Temperature-Sensor-PC** -> Aplicação em Java para acionar o sensor de temperatura do PC. Usado quando o sistema IFA executa localmente no PC ao invés do CC. Simula o hardware do sensor de temperatura. [[Temperature-Sensor-PC](./Temperature-Sensor-PC/)]
* **UAV-Ensemble-GA-GA_GA-GH** -> Implementação em Java dos algoritmos de comitê GA-GA e GA-GH executados em paralelo (usado pelo IFA). [[UAV-Ensemble-GA-GA_GA-GH](./UAV-Ensemble-GA-GA_GA-GH/)]
* **UAV-Exec-PathReplanner-Massive** -> Implementação em Java de execuções massivas de replanejamento de rota (usado pelo UAV-Fixed-Route4s). [[UAV-Exec-PathReplanner-Massive](./UAV-Exec-PathReplanner-Massive/)]
* **UAV-Pre-Planned4s** -> Implementação em Java para definição da melhor rota de pouso emergencial em um conjunto de rotas (usado pelo IFA). [[UAV-Pre-Planned4s](./UAV-Pre-Planned4s/)]
