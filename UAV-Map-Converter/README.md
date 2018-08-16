# UAV-Map-Converter

Este projeto faz uma conversão entre os formatos de mapa .SGL para .JSON e .XML.
As instâncias de mapas podem ser tanto artificiais quanto reais. 

## Como Executar: 

* **Command:** 
`java -jar UAV-Map-Converter.jar --help`

* **Command:** 
```
java -jar UAV-Map-Converter.jar DIR TYPE_MAP PRINT_PRETTY_FORMAT TYPE_CONVERSION 
	DIR -> dir where are the maps.
		Example: mission/
	TYPE_MAP -> 1 - NFZ Map			2 - Full Map
		Example: 1
	PRINT_PRETTY_FORMAT -> true or false
		Example: true
	TYPE_CONVERSION -> JSON or XML
		Example: JSON
```

## Formato de Entrada: 

* **SGL** -> formato de entrada próprio do mapa, utilizado pelo Framework ProOF. Este padrão é mais leve, porém também mais difícil de entender e de ler.

## Formato de Saída: 

* **JSON** -> formato de saída do mapa é, em geral, fácil de entender e ler. Este padrão é mais pesado que o SGL, ocupa em média o quádruplo do SGL em disco (HD).
* **XML** -> formato de saída do mapa é, em geral, fácil de ler. Este padrão é mais pesado que o SGL e JSON, em geral, ocupa o dobro do tamanho do JSON em disco (HD).

## Arquivo de Instância do Path Planner:

Os valores das coordenadas utilizadas são cartesianas.
Utilizado no planejador de rotas: HGA4m.

Arquivo de Entrada em .SGL.

```
<number of polygons>
2
<x..., y..., n = 4, id = 0, type = nfz>
-0.1574530915090589,35.683493236344965,35.76497352100013,-0.29027759355372845
0.18960489221798285,-0.20113919146741488,-3.590007201222733,-3.1303520957103608
<x..., y..., n = 4, id = 0, type = nfz>
-0.13697045585684411,2.7159695135371034,2.688223095078793,-0.006680790582055828
-2.8846638150184924,-2.92419757186272,-9.645645524801244,-9.645732521797555
```

Arquivo de saída em .JSON.

```
{
  "regions": [
    {
      "nameRegion": "0",
      "typeRegion": "NFZ_REGION",
      "coordenates": [
        {
          "x": -0.1574530915090589,
          "y": 0.18960489221798285
        },
        {
          "x": 35.683493236344965,
          "y": -0.20113919146741488
        },
        {
          "x": 35.76497352100013,
          "y": -3.590007201222733
        },
        {
          "x": -0.29027759355372845,
          "y": -3.1303520957103608
        }
      ]
    },
    {
      "nameRegion": "1",
      "typeRegion": "NFZ_REGION",
      "coordenates": [
        {
          "x": -0.13697045585684411,
          "y": -2.8846638150184924
        },
        {
          "x": 2.7159695135371034,
          "y": -2.92419757186272
        },
        {
          "x": 2.688223095078793,
          "y": -9.645645524801244
        },
        {
          "x": -0.006680790582055828,
          "y": -9.645732521797555
        }
      ]
    }
  ]
}
```

Arquivo de saída em .XML.

```
<uav.map.converter.map.Map>
  <regions>
    <uav.map.converter.map.Poly2D>
      <nameRegion>0</nameRegion>
      <typeRegion>NFZ_REGION</typeRegion>
      <coordenates>
        <uav.map.converter.map.Point2D>
          <x>-0.1574530915090589</x>
          <y>0.18960489221798285</y>
        </uav.map.converter.map.Point2D>
        <uav.map.converter.map.Point2D>
          <x>35.683493236344965</x>
          <y>-0.20113919146741488</y>
        </uav.map.converter.map.Point2D>
        <uav.map.converter.map.Point2D>
          <x>35.76497352100013</x>
          <y>-3.590007201222733</y>
        </uav.map.converter.map.Point2D>
        <uav.map.converter.map.Point2D>
          <x>-0.29027759355372845</x>
          <y>-3.1303520957103608</y>
        </uav.map.converter.map.Point2D>
      </coordenates>
    </uav.map.converter.map.Poly2D>
    <uav.map.converter.map.Poly2D>
      <nameRegion>1</nameRegion>
      <typeRegion>NFZ_REGION</typeRegion>
      <coordenates>
        <uav.map.converter.map.Point2D>
          <x>-0.13697045585684411</x>
          <y>-2.8846638150184924</y>
        </uav.map.converter.map.Point2D>
        <uav.map.converter.map.Point2D>
          <x>2.7159695135371034</x>
          <y>-2.92419757186272</y>
        </uav.map.converter.map.Point2D>
        <uav.map.converter.map.Point2D>
          <x>2.688223095078793</x>
          <y>-9.645645524801244</y>
        </uav.map.converter.map.Point2D>
        <uav.map.converter.map.Point2D>
          <x>-0.006680790582055828</x>
          <y>-9.645732521797555</y>
        </uav.map.converter.map.Point2D>
      </coordenates>
    </uav.map.converter.map.Poly2D>
  </regions>
</uav.map.converter.map.Map>
```

## Arquivo de Instância do Path Replanner:

Os valores das coordenadas utilizadas são cartesianas.
Utilizado no replanejador de rotas: MPGA4s, GA4s, GH4s, DE4s, etc.

Arquivo de Entrada em .SGL.

```
<number of polygons>
3
<number of nfz>
1
<number of penalty zone>
1
<number of bonus zone>
1
<x..., y..., n = 4, id = 0, type = nfz>
2.3829010559938983,49.24115061768672,49.189630319136874,2.3003630093505167
36.73868927635811,36.66755891079154,34.330884858933295,34.19151035375383
<x..., y..., n = 4, id = 0, type = penalty zone>
-0.41900629188239247,2.3239958676009502,2.562487255925019,-0.3594149749669098
36.89356242727499,36.89355522135072,2.8420776150670823,2.842076853847313
<x..., y..., n = 4, id = 0, type = bonus>
5.164413284046982,11.78270493610218,12.102424108805435,5.302163945459722
33.00164862752377,33.11035656792369,26.147003218701542,26.147011223748652
```

Arquivo de saída em .JSON.

```
{
  "regions": [
    {
      "nameRegion": "0",
      "typeRegion": "NFZ_REGION",
      "coordenates": [
        {
          "x": 2.3829010559938983,
          "y": 36.73868927635811
        },
        {
          "x": 49.24115061768672,
          "y": 36.66755891079154
        },
        {
          "x": 49.189630319136874,
          "y": 34.330884858933295
        },
        {
          "x": 2.3003630093505167,
          "y": 34.19151035375383
        }
      ]
    },
    {
      "nameRegion": "0",
      "typeRegion": "PENALTY_REGION",
      "coordenates": [
        {
          "x": -0.41900629188239247,
          "y": 36.89356242727499
        },
        {
          "x": 2.3239958676009502,
          "y": 36.89355522135072
        },
        {
          "x": 2.562487255925019,
          "y": 2.8420776150670823
        },
        {
          "x": -0.3594149749669098,
          "y": 2.842076853847313
        }
      ]
    },
    {
      "nameRegion": "0",
      "typeRegion": "BONUS_REGION",
      "coordenates": [
        {
          "x": 5.164413284046982,
          "y": 33.00164862752377
        },
        {
          "x": 11.78270493610218,
          "y": 33.11035656792369
        },
        {
          "x": 12.102424108805435,
          "y": 26.147003218701542
        },
        {
          "x": 5.302163945459722,
          "y": 26.147011223748652
        }
      ]
    }
  ]
}
```

Arquivo de saída em .XML.

```
<uav.map.converter.map.Map>
  <regions>
    <uav.map.converter.map.Poly2D>
      <nameRegion>0</nameRegion>
      <typeRegion>NFZ_REGION</typeRegion>
      <coordenates>
        <uav.map.converter.map.Point2D>
          <x>2.3829010559938983</x>
          <y>36.73868927635811</y>
        </uav.map.converter.map.Point2D>
        <uav.map.converter.map.Point2D>
          <x>49.24115061768672</x>
          <y>36.66755891079154</y>
        </uav.map.converter.map.Point2D>
        <uav.map.converter.map.Point2D>
          <x>49.189630319136874</x>
          <y>34.330884858933295</y>
        </uav.map.converter.map.Point2D>
        <uav.map.converter.map.Point2D>
          <x>2.3003630093505167</x>
          <y>34.19151035375383</y>
        </uav.map.converter.map.Point2D>
      </coordenates>
    </uav.map.converter.map.Poly2D>
    <uav.map.converter.map.Poly2D>
      <nameRegion>0</nameRegion>
      <typeRegion>PENALTY_REGION</typeRegion>
      <coordenates>
        <uav.map.converter.map.Point2D>
          <x>-0.41900629188239247</x>
          <y>36.89356242727499</y>
        </uav.map.converter.map.Point2D>
        <uav.map.converter.map.Point2D>
          <x>2.3239958676009502</x>
          <y>36.89355522135072</y>
        </uav.map.converter.map.Point2D>
        <uav.map.converter.map.Point2D>
          <x>2.562487255925019</x>
          <y>2.8420776150670823</y>
        </uav.map.converter.map.Point2D>
        <uav.map.converter.map.Point2D>
          <x>-0.3594149749669098</x>
          <y>2.842076853847313</y>
        </uav.map.converter.map.Point2D>
      </coordenates>
    </uav.map.converter.map.Poly2D>
    <uav.map.converter.map.Poly2D>
      <nameRegion>0</nameRegion>
      <typeRegion>BONUS_REGION</typeRegion>
      <coordenates>
        <uav.map.converter.map.Point2D>
          <x>5.164413284046982</x>
          <y>33.00164862752377</y>
        </uav.map.converter.map.Point2D>
        <uav.map.converter.map.Point2D>
          <x>11.78270493610218</x>
          <y>33.11035656792369</y>
        </uav.map.converter.map.Point2D>
        <uav.map.converter.map.Point2D>
          <x>12.102424108805435</x>
          <y>26.147003218701542</y>
        </uav.map.converter.map.Point2D>
        <uav.map.converter.map.Point2D>
          <x>5.302163945459722</x>
          <y>26.147011223748652</y>
        </uav.map.converter.map.Point2D>
      </coordenates>
    </uav.map.converter.map.Poly2D>
  </regions>
</uav.map.converter.map.Map>
```
