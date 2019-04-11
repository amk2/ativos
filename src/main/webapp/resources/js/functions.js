/*
 * Sling Control 1.0.0
 * www.enplage.com.br
*/



/*** Funções executadas durante o load da página ***/

function menuHorizontal() {
 
   var navItems = document.getElementById("menu_dropdown").getElementsByTagName("li");
    
   for (var i=0; i< navItems.length; i++) {
      if(navItems[i].className == "submenu")
      {
         if(navItems[i].getElementsByTagName('ul')[0] != null)
         {
            navItems[i].onmouseover=function() {
				this.getElementsByTagName('ul')[0].style.display="block";
				this.getElementsByTagName('a')[0].style.backgroundColor = "#C2D5EB";
			}
            navItems[i].onmouseout=function() {
				this.getElementsByTagName('ul')[0].style.display="none";
				this.getElementsByTagName('a')[0].style.backgroundColor = "";
			}
         }
      }
   }
 
}