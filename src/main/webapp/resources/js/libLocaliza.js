/**
 * Biblioteca para manipula��o de mapas utilizando Google Maps
 * Autor: Rafael de Paula
 * (C)2011 Enplage Solu��es Inovadoras
 */

var overlay;
var map;
var activeInfoWindow;
		
function mapLocalInit(pLatitude, pLongitude, pNomeElemento, pNomeAtivo, pConteudo) {

	// Coordenadas de localiza��o do ativo
	var CoordenadasLatLng = new google.maps.LatLng(pLatitude, pLongitude);

	// Op��es para exibi��o do mapa
	var OpcoesMapa = {
		zoom: 18,
		center: CoordenadasLatLng,
		mapTypeId: google.maps.MapTypeId.HYBRID
	}

	// Cria��o do objeto
	map = new google.maps.Map(document.getElementById(pNomeElemento), OpcoesMapa); 

	// Mapa customizado para exibi��o sobreposta ao Google Maps
	// As coordenadas de latitude e longitude representam a �rea do Porto
	MAP.Utils.overlayAdd("img_mapa/mapa.png", -22.932215, -43.843140, -22.92120, -43.818858); 

	// Padroniza��o do marcador
	var MarcadorImg = new google.maps.MarkerImage("img_mapa/marcador.png",
								new google.maps.Size(32, 31),
								new google.maps.Point(0,0),
								new google.maps.Point(16, 31)
					);

	// Padroniza��o da sombra do marcador
	var MarcadorSmb = new google.maps.MarkerImage("img_mapa/marcador_sombra.png",
								new google.maps.Size(64, 52),
								new google.maps.Point(0,0),
								new google.maps.Point(-5, 42)
					);
	
	// Forma de sobreposi��o
	var shape = {
		coord: [1, 1, 1, 20, 18, 20, 18 , 1],
		type: "poly"
	};	

	// Marcador
	var MarcadorInfo = new google.maps.InfoWindow({
							content: pConteudo
						});
	
	var Marcador = new google.maps.Marker({
							position: CoordenadasLatLng,
							map: map,
							title: pNomeAtivo,
							shadow: MarcadorSmb,
							icon: MarcadorImg
						});

	google.maps.event.addListener(Marcador, "click", function() {
		if(activeInfoWindow){
			activeInfoWindow.close();
		}	
		MarcadorInfo.open(map, Marcador);
		activeInfoWindow = MarcadorInfo;
	});

	google.maps.event.addListener(Marcador, "dblclick", function() {
		var mapcenter = Marcador.get_position();
		MAP.Utils.centerMap(mapcenter);
		map.set_zoom(19);
	});
	
}	