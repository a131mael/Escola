                                
// abc123
function MascaraCodBar(campo, e){
    
    //apagar campo
    var whichCode = (window.Event) ? e.which : e.keyCode;
    if (whichCode == 13 || whichCode == 8){
        alert(2);
        return false;
    }else{
        campo.value = campo.value.replace(/[^a-zA-Z0-9\s]/, '');
        return true;              
    }
    
}
