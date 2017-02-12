                                
// A C  
function MascaraTipoiDoenca(campo, e){
    
    var key = "";
    var whichCode = (window.Event) ? e.which : e.keyCode;
    key = String.fromCharCode(whichCode);
    
    if (key == a || key == c || key == A || key == C){
        alert(2);
        return true;
    }else{
        return false;              
    }
    
}
