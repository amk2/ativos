// Author: Gary Evans gary@wholewheatcreative.com
// URL: http://wholewheatcreative.com
// DATE: 07/01/2009
// Co-Author: Travis Beck travis@wholewheatcreative.com
// Special Thanks: John D. Coryat
// USNaviguide LLC - http://www.usnaviguide.com

/* -----------------------------------*/
/* ---------->>> MAP API <<<----------*/
/* -----------------------------------*/

var MAP = {};

/* -----------------------------------*/
/* ---------->>> GLOBAL <<<-----------*/
/* -----------------------------------*/

MAP.Global = {
   mapKey: 'ABQIAAAA9Mf-UdCWLoQh3zP7iGycYBTGHive17i7-nCfYwu94Jm3lo_9yBRwf0gUj9QtTtn3NwgqCRDfuY9iaQ',
   northMapCoords: '',
   centralMapCoords: '',  
   southMapCoords: '',  
   programload: ''
}

/* -----------------------------------*/
/* ---------->>> EVENTS <<<-----------*/
/* -----------------------------------*/
MAP.Events = {
	
mapModalSetup: function(){
      $('.dropdown').each(function(i, el){
         $(el).children('span').click(function(o){
            o.preventDefault();
         }).mouseover(function(){
            $($(this).children('ul')).css('display', 'block');
         }).mouseout(function(){
            $($(this).children('ul')).css('display', 'none');
         });
      });
      
      $('.dropdown li').each(function(i, el){
         $(el).click(function(){
            $(this).parents('span').children(':first').html($(this).html());
            $(this).parents('ul').css('display', 'none');
				switch($(this).attr('rel')){
					case 'n':
						var myLatlngN = new google.maps.LatLng(29.80982123917413, -95.18011808395386);
					 	MAP.Utils.centerMap(myLatlngN);
					break;
					case 'c':
						var myLatlngC = new google.maps.LatLng(29.65766794572544, -95.11462926864624);
						 MAP.Utils.centerMap(myLatlngC);
					break;
					case 's':
						var myLatlngS = new google.maps.LatLng(29.580138, -95.202788);
					 	MAP.Utils.centerMap(myLatlngS);
					break;
					default:
						var myLatlngD = new google.maps.LatLng(29.80982123917413, -95.18011808395386);
						MAP.Utils.centerMap(myLatlngD);
					break;
				}				
         });
      });
   }
}

/* -----------------------------------*/
/* -------->>> UTILITIES <<<----------*/
/* -----------------------------------*/

MAP.Utils = {
	
	centerMap: function(location){
  		map.setCenter(location);  
	},
  
	createIcon: function (imagePath, lat, long){
		var image = new google.maps.MarkerImage(imagePath,
		// This marker is 20 pixels wide by 32 pixels tall.
		new google.maps.Size(44, 32),
		// The origin for this image is 0,0.
		new google.maps.Point(0,0),
		// The anchor for this image is the base of the flagpole at 0,32.
		new google.maps.Point(22, 16));
	
		var myLatLng = new google.maps.LatLng(lat, long);
		var theMarker = new google.maps.Marker({
			position: myLatLng,
			map: map,
			icon: image,
			size: new google.maps.Size(65,65)
		});
	},
	 
   overlayAdd: function(url, nlat, nlng, slat, slng){
		 var ne = new google.maps.LatLng(nlat, nlng)
		 var sw = new google.maps.LatLng(slat, slng) ;
		 var bounds = new google.maps.LatLngBounds(sw,ne) ;
		 overlay = new MAP.Utils.projectedOverlay(map, url, bounds, {}) ;
   },

   overlayRemove: function(){
   	if ( !overlay ){
   	 return ;
  	  }
		overlay.remove() ;
		overlay = null ;
   },

	projectedOverlay: function(map, imageUrl, bounds, opts){
		
	 google.maps.OverlayView.call(this);

	 this.map_ = map;
	 this.url_ = imageUrl ;
	 this.bounds_ = bounds ;
	 this.addZ_ = opts.addZoom || '' ;			
	 this.id_ = opts.id || this.url_ ;			
	 this.percentOpacity_ = opts.percentOpacity || 80 ;

	 this.setMap(map);
	}

};


/* -----------------------------------*/
/* ----------->>> PTYPE <<<-----------*/
/* -----------------------------------*/

MAP.Utils.projectedOverlay.prototype = new google.maps.OverlayView();

MAP.Utils.projectedOverlay.prototype.createElement = function(){
 var panes = this.getPanes() ;
 var div = this.div_ ;

 if (!div){
  div = this.div_ = document.createElement("div");
  div.style.position = "absolute" ;
  div.setAttribute('id',this.id_) ;
  this.div_ = div ;
  this.lastZoom_ = -1 ;
  if( this.percentOpacity_ ){
   this.setOpacity(this.percentOpacity_) ;
  }
  panes.overlayLayer.appendChild(div);
 }
}


MAP.Utils.projectedOverlay.prototype.remove = function(){
 if (this.div_) {
  this.setMap(null);
  this.div_.parentNode.removeChild(this.div_);
  this.div_ = null;
 }
}

MAP.Utils.projectedOverlay.prototype.draw = function(firstTime){
 this.createElement();
 if (!this.div_){
  return ;
 }

 var c1 = this.get('projection').fromLatLngToDivPixel(this.bounds_.getSouthWest());
 var c2 = this.get('projection').fromLatLngToDivPixel(this.bounds_.getNorthEast());

 if (!c1 || !c2) return;

 this.div_.style.width = Math.abs(c2.x - c1.x) + "px";
 this.div_.style.height = Math.abs(c2.y - c1.y) + "px";
 this.div_.style.left = Math.min(c2.x, c1.x) + "px";
 this.div_.style.top = Math.min(c2.y, c1.y) + "px";

 if ( this.lastZoom_ == this.map_.getZoom() ){
  return ;
 }

 this.lastZoom_ = this.map_.getZoom() ;

 var url = this.url_ ;

 if ( this.addZ_ ){
  url += this.addZ_ + this.map_.getZoom() ;
 }
 this.div_.innerHTML = '<img src="' + url + '"  width=' + this.div_.style.width + ' height=' + this.div_.style.height + ' >' ;
}

MAP.Utils.projectedOverlay.prototype.setOpacity=function(opacity)
{
 if (opacity < 0){
  opacity = 0 ;
 }
 if(opacity > 100){
  opacity = 100 ;
 }
 var c = opacity/100 ;

 if (typeof(this.div_.style.filter) =='string'){
  this.div_.style.filter = 'alpha(opacity:' + opacity + ')' ;
 }
 if (typeof(this.div_.style.KHTMLOpacity) == 'string' ){
  this.div_.style.KHTMLOpacity = c ;
 }
 if (typeof(this.div_.style.MozOpacity) == 'string'){
  this.div_.style.MozOpacity = c ;
 }
 if (typeof(this.div_.style.opacity) == 'string'){
  this.div_.style.opacity = c ;
 }
}

/* -----------------------------------*/
/* ----------->>> INIT <<<------------*/
/* -----------------------------------*/
