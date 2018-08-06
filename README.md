# UAV-Tools

<p align="center">
  <a href="#">
    <img src="https://img.shields.io/badge/UAV-TOOLS-brightgreen.svg" alt="UAV-Tools">
  </a>
  <a href="https://github.com/jesimar/UAV-Tools/blob/master/LICENSE" target="_blank">
    <img src="https://img.shields.io/aur/license/yaourt.svg" alt="License">
  </a>
  <a href="https://github.com/jesimar/UAV-Tools/pulse" target="_blank">
    <img src="https://img.shields.io/github/downloads/jesimar/UAV-Tools/total.svg" alt="Downloads">
  </a>
</p>

Conjunto de ferramentas desenvolvidas para automatização de voos de Veículos Aéreos Não-Tripulados (VANTs) ou *Unmanned Aerial Vehicles* (UAVs).

## Visão Geral

Nesse projeto podemos encontrar os seguintes diretórios:

* **CreateMapArtificial** -> Código em Java que gera um conjunto de mapas artificiais para os problemas de Planejamento/Replanejamento de Rotas de VANTs. [[CreateMapArtificial](./CreateMapArtificial/)]
* **ManagerSITL** -> Software em Java que auxilia na automatização de simulações Software-in-the-Loop (SILT) usando o ArduPilot SITL. [[ManagerSITL](./ManagerSITL/)]
* **Missions-Ardupilot-SITL** -> Agrupa um conjunto de missões para serem simuladas no ardupilot SITL.[[Missions-Ardupilot-SITL](./Missions-Ardupilot-SITL/)]
* **ProcessDataFG** -> Software em Java que extrai informações através de imagens de múltiplas execuções do AutoFG. [[ProcessDataFG](./ProcessDataFG/)]
* **Simulations-AutoFG-Waypoints** -> Sistema de piloto automático desenvolvido por Marcelo Hossomi. [[Simulations-AutoFG-Waypoints](./Simulations-AutoFG-Waypoints/)]
* **UAV-Ensemble-GA-GA_GA-GH** -> Implementação em Java dos algoritmos de comitê GA-GA e GA-GH executados em paralelo (usado pelo IFA). [[UAV-Ensemble-GA-GA_GA-GH](./UAV-Ensemble-GA-GA_GA-GH/)]
* **UAV-Exec-PathReplanner-Massive** -> Implementação em Java de execuções massivas de replanejamento de rota (usado pelo UAV-Fixed-Route4s). [[UAV-Exec-PathReplanner-Massive](./UAV-Exec-PathReplanner-Massive/)]
* **UAV-Fixed-Route4s** -> Implementação em Java para definição da melhor rota de pouso emergencial em um conjunto de rotas (usado pelo IFA). [[UAV-Fixed-Route4s](./UAV-Fixed-Route4s/)]
* **UAV-Plot-Mission** -> Sistema para plotar o mapa da missão e sua rota. [[UAV-Plot-Mission](./UAV-Plot-Mission/)]
* **UAV-Route3DToGeo** -> Projeto em Java que converte uma rota em coordenadas cartesianas para coordenadas geográficas. [[UAV-Route3DToGeo](./UAV-Route3DToGeo/)]
* **UAV-Toolkit-C** -> Conjunto de códigos em C para gerenciamento do drone. [[UAV-Toolkit-C](./UAV-Toolkit-C/)]
* **Util-Spreadsheets** -> Conjunto de planilhas para diversos utilidades. [[Util-Spreadsheets](./Util-Spreadsheets/)]

## Contributors

Os principais contribuidores desse projeto podem ser encontrados [aqui](https://github.com/jesimar/UAV-Tools/blob/master/AUTHORS)

## Changelog

A versão do ChangeLog pode ser acessado [aqui](https://github.com/jesimar/UAV-Tools/blob/master/CHANGELOG.md). 

## Licença

UAV-Tools está disponível sobre código aberto com permissões [GNU General Public License v3.0](https://github.com/jesimar/UAV-Tools/blob/master/LICENSE). 

