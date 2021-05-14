# INF273_MetaHeuristicProject

A program using Metaheuristics to compute the Pickup and Delivery problem with time windows.




<h2>How to use</h2>

1. Download or clone the files to an IDE.
2. 





<h2>Input</h2>

Input is in the form of a .txt file, containing:
* The number of total nodes
* The number of vehicles
* The vehicles (with an ID, an initial node, a starting time, and a maximum carrying capacity)
* The number of calls to be handled
* Each vehicle, with the IDs of calls that are compatible with that vehicle
* The calls to be handled (ID, origin node, destination node, cargo size, cost of outsourcing,
lower time bound for pickup, upper time bound for pickup, lower time bound for delivery, upper time bound for delivery)
* The travel times and costs between each node (vehicle ID, origin node, destination node, travel time, travel cost)
* The information related to picking up and delivering cargo (vehicle ID, call ID, how long it takes to pickup cargo,
monetary cost of picking up cargo, how long it taks to deliver/unload cargo, monetary cost of delivering cargo).


<h3>Simplified example of an input file:</h3>

%  number of nodes<br>
39<br>
%  number of vehicles<br>
3<br>
%  for each vehicle: vehicle index, home node, starting time, capacity<br>
1,8,125,13200<br>
2,13,106,13200<br>
3,31,0,16500<br>
% number of calls<br>
7<br>
%  for each vehicle, vehicle index, and then a list of calls that can be transported using that vehicle<br>
1,1,3,7<br>
2,1,3,7<br>
3,1,2,3,4,5,6,7<br>
% for each call: call index, origin node, destination node, size, cost of not transporting, lowerbound timewindow for pickup, upper_timewindow for pickup, lowerbound timewindow for delivery, upper_timewindow for delivery<br>
1,17,37,4601,790000,345,417,345,1006<br>
2,33,36,13444,430790,96,168,96,529<br>
.<br>
.<br>
.<br>
7,26,6,5310,359885,178,250,178,567<br>
%  travel times and costs: vehicle, origin node, destination node, travel time, travel cost<br>
1,1,1,0,0<br>
2,1,1,0,0<br>
3,1,1,0,0<br>
1,1,2,71,48031<br>
2,1,2,71,48031<br>
3,1,2,66,38871<br>
1,1,3,19,12930<br>
2,1,3,19,12930<br>
3,1,3,18,10464<br>
.<br>
.<br>
.<br>
1,39,38,75,50672<br>
2,39,38,75,50672<br>
3,39,38,70,41008<br>
1,39,39,0,0<br>
2,39,39,0,0<br>
3,39,39,0,0<br>
%  node times and costs: vehicle, call, origin node time, origin node costs, destination node time, destination node costs<br>
1,1,13,23768,16,29040<br>
1,2,-1,-1,-1,-1<br>
1,3,17,23768,19,28042<br>
1,4,-1,-1,-1,-1<br>
1,5,-1,-1,-1,-1<br>
1,6,-1,-1,-1,-1<br>
1,7,18,30200,18,29828<br>
.<br>
.<br>
.<br>
3,6,31,23893,37,33227<br>
3,7,18,31850,18,31478<br>
% EOF<br>
