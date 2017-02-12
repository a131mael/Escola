                                
// {31/12/2013}
function MascaraData(campoData, e){
    //apagar campo
    var whichCode = (window.Event) ? e.which : e.keyCode;
    if (whichCode == 13 || whichCode == 8){
        return true;
    }
    var data = campoData.value;
    if (data.length == 2){
        data = data + '/';
        campoData.value = data;
        return true;              
    }
    if (data.length == 5){
        data = data + '/';
        campoData.value = data;
        return true;
    }
    if(data.length > 9){
        return false;
    }
}
