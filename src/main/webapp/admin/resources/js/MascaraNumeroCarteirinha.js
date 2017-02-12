                                
// 123...
function MascaraNumeroInteiro(campo, e){
    //apagar campo
    var whichCode = (window.Event) ? e.which : e.keyCode;
    if (whichCode == 13 || whichCode == 8){
        return true;
    }else{
        campo.value = campo.value.replace(/[^0-9]/gi, '');
        return true;              
    }
    
}
