
//target somente os inputs
function eventoTab(campo,e){
    var n = $("input:text").length;
    if (e.which == 9) { //tecla TAB
        e.preventDefault(); //ignorar comportamento padrao
        var nextIndex = $('input:text').index(campo) + 1;
        if( n > nextIndex)
            $('input:text')[nextIndex].focus();
        else{
            $('input:text:first').focus();
        }
    }
}

//troca o foco com enter 
function eventoEnter(campo, e){
    var n = $("input:text").length;
    if (e.which == 13 || e.which == 9) { //tecla Enter
        e.preventDefault(); //ignorar comportamento padrao
       
        var nextIndex = $('input:text').index(campo) + 1;
        if( n > nextIndex){
            $('input:text')[nextIndex].focus();
            $('input:text')[nextIndex].select();
        }else{
            $('input:text:first').focus();
            $('input:text:first').select();
        }
        ''
    }
}

function eventoIgnorarComportamentoPadrao(campo , e , codigo1){
     if (e.which == codigo1) {
        e.preventDefault(); //ignorar comportamento padrao
    }
}

//troca o foco com enter ou tab, da via verificando se vai para proximo componente ou nao
function eventoTrocaFocoVia(campo, e, target){
    var n = $("input:text").length;
    if (e.which == 13 || e.which == 9) { //tecla Enter
        e.preventDefault(); //ignorar comportamento padrao
        if(campo.value == '' || campo.value == ' '){
            document.getElementById(target).focus();
            document.getElementById(target).select();
        }else{
            var nextIndex = $('input:text').index(campo) + 1;
            if( n > nextIndex){
                $('input:text')[nextIndex].focus();
                $('input:text')[nextIndex].select();
            }else{
                $('input:text:first').focus();
                $('input:text:first').select();
            }
        }
       
    }
}

//troca o foco com enter 
function eventoTrocaCampo(campo, e){
    var n = $("input:text").length;
    if (e.which == 13 || e.which == 9) { //tecla Enter
        e.preventDefault(); //ignorar comportamento padrao
       
        var nextIndex = $('input:text').index(campo) + 1;
        if( n > nextIndex){
            $('input:text')[nextIndex].focus();
            $('input:text')[nextIndex].select();
        }else{
            $('input:text:first').focus();
            $('input:text:first').select();
        }
        ''
    }
}

//troca o foco com enter 
function eventoEnterPularParaCampo(target, e){
    if (e.which == 13 || e.which == 9) { //tecla Enter
        e.preventDefault(); //ignorar comportamento padrao
        document.getElementById(target).focus();
        document.getElementById(target).select();
    }
}

function eventoVia(campo,e,idGp){
    var n = $("input:text").length;

    if(e.which == 38){ // seta para cima
        e.preventDefault();
        trocaVia(([{
            name:'indiceGp', 
            value:idGp
        }]));
    }else if(e.which == 40){ // seta para baixo
        e.preventDefault();
        trocaVia(([{
            name:'indiceGp', 
            value:idGp
        }]));
    }else if (e.which == 13) { //tecla Enter
        e.preventDefault(); //ignorar comportamento padrao
        var nextIndex = $('input:text').index(campo) + 1;
        if( n > nextIndex){
            $('input:text')[nextIndex].focus();
            $('input:text')[nextIndex].select();
        }else{
            $('input:text:first').focus();
            $('input:text')[nextIndex].select();
        }
        ''
    }
}
function eventoParticipacao(campo,e){
    var n = $("input:text").length;
         
    if(e.which == 38){ // seta para cima
        trocaParticipacao();
    }else if(e.which == 40){ // seta para baixo
        trocaParticipacao();
    }else if (e.which == 13) { //tecla Enter
        e.preventDefault(); //ignorar comportamento padrao
        var nextIndex = $('input:text').index(campo) + 1;
        if( n > nextIndex)
            $('input:text')[nextIndex].focus();
        else{
            $('input:text:first').focus();
        }
        ''
    }
}

function eventoAcomodacao(campo,e){
    var n = $("input:text").length;
                
    if(e.which == 38){ // seta para cima
        trocaAcomodacao();
    }else if(e.which == 40){ // seta para baixo
        trocaAcomodacao();
    }else if (e.which == 13) { //tecla Enter
        e.preventDefault(); //ignorar comportamento padrao
        var nextIndex = $('input:text').index(campo) + 1;
        if( n > nextIndex)
            $('input:text')[nextIndex].focus();
        else{
            $('input:text:first').focus();
        }
        ''
    }
}
function eventoTrocaTipoGuia(campo,e){
    var n = $("input:text").length;
       
    if(e.which == 38){ // seta para cima
        trocaTipoGuia();
    }else if(e.which == 40){ // seta para baixo
        trocaTipoGuia();
    }else if (e.which == 13) { //tecla Enter
        e.preventDefault(); //ignorar comportamento padrao
        var nextIndex = $('input:text').index(campo) + 1;
        if( n > nextIndex)
            $('input:text')[nextIndex].focus();
        else{
            $('input:text:first').focus();
        }
        ''
    }
}

function eventoPulaCampoAutomatico(quantidade,campo,e){

    var n = $("input:text").length;
    var quantidadeNumeros = campo.value.replace(/[^\d]+/g,'').length;
    if((e.which>95 && e.which<106) || (e.which>47 && e.which<58) && quantidade > 0){
        
        if(quantidadeNumeros == quantidade){
       
            e.preventDefault(); //ignorar comportamento padrao
            var nextIndex = $('input:text').index(campo) + 1;
            if( n > nextIndex){
                $('input:text')[nextIndex].focus();
                $('input:text')[nextIndex].select();
            }else{
                $('input:text:first').focus();
                $('input:text')[nextIndex].select();
            }
        }
    }else if(e.which == 13){
        e.preventDefault(); //ignorar comportamento padrao
    }
}

function eventoVoltaCampoAutomatico(quantidade,campo,e){

    var n = $("input:text").length;
    var quantidadeNumeros = campo.value.replace(/[^\d]+/g,'').length;
    if((e.which>95 && e.which<106) || (e.which>47 && e.which<58) && quantidade > 0){
        
        if(quantidadeNumeros == quantidade){
       
            var nextIndex = $('input:text').index(campo) - 1;
            if( n > nextIndex){
                $('input:text')[nextIndex].focus();
                $('input:text')[nextIndex].select();
            }else{
                $('input:text:first').focus();
            }
        }
    }else if(e.which == 13){
        e.preventDefault(); //ignorar comportamento padrao
    }
}

function eventoPulaCampoAutomaticoMatricula(quantidade,campo,e){

    var n = $("input:text").length;
    var quantidadeNumeros = campo.value.replace(/[^\d]+/g,'').length;
    if(( (e.which>95 && e.which<106) || (e.which>47 && e.which<58)) && quantidade > 1){
        
        if(quantidadeNumeros == quantidade){
       
            e.preventDefault(); //ignorar comportamento padrao
            var nextIndex = $('input:text').index(campo) + 1;
            if( n > nextIndex)
                $('input:text')[nextIndex].focus();
            else{
                $('input:text:first').focus();
            }
        }
    }else if(e.which == 13){
        e.preventDefault(); //ignorar comportamento padrao
    }
}

//troca o foco com enter 
function eventoEnterTec(campo,e){
    var n = $("input:text").length;
    if (e.which == 13) { //tecla Enter
        e.preventDefault(); //ignorar comportamento padrao
        var nextIndex = $('input:text').index(campo) + 6;
        $('input:text')[nextIndex].focus();
        //        document.formPrincipal.btAdicionar.focus();
        ''
    }
}

function eventoTec(campo,e,idGp){
    var n = $("input:text").length;
    if(e.which == 38){ // seta para cima
        trocaTec(([{
            name:'indiceGp', 
            value:idGp
        }]));
    }else if(e.which == 40){ // seta para baixo
        trocaTec(([{
            name:'indiceGp', 
            value:idGp
        }]));
    }else if (e.which == 13) { //tecla Enter
        e.preventDefault(); //ignorar comportamento padrao
        adicionaProcedimento();
        
        var nextIndex = $('input:text').index(campo) + 1;
        $('input:text')[nextIndex].focus();
        
        //            document.formPrincipal.btAdicionarSemValidar.focus();
        ''
    }
}

function eventoTrocaBil(campo,e,idGp){
    var n = $("input:text").length;

    if(e.which == 38){ // seta para cima
        trocaBil(([{
            name:'indiceGp', 
            value:idGp
        }]));
    }else if(e.which == 40){ // seta para baixo
        trocaBil(([{
            name:'indiceGp',  
            value:idGp
        }]));
    }else if (e.which == 13) { //tecla Enter
        e.preventDefault(); //ignorar comportamento padrao
        var nextIndex = $('input:text').index(campo) + 1;
        if( n > nextIndex){
            $('input:text')[nextIndex].focus();
            $('input:text')[nextIndex].select();
        }else{
            $('input:text:first').focus();
            $('input:text')[nextIndex].select();
        }
        ''
    }
}

//troca o foco com enter 
function eventoEnterSemTrocaDeCampo(campo,e){
    var n = $("input:text").length;
    if (e.which == 13) { //tecla Enter
        e.preventDefault(); //ignorar comportamento padrao
        if( n > nextIndex)
            $('input:text')[nextIndex].focus();
        else{
            $('input:text:first').focus();
        }
        ''
    }
}

//troca o foco com enter 
function eventoBlurUp(campo,e){
    var n = $("input:text").length;
    if (e.which == 13) { //tecla Enter
        e.preventDefault(); //ignorar comportamento padrao
        var nextIndex = $('input:text').index(campo);
        $('input:text')[nextIndex].focus();
    }
}

//troca o foco com enter 
function eventoEnterCampo(campo,e){
    var n = $("input:text").length;
    if (e.which == 13) { //tecla Enter
        e.preventDefault(); //ignorar comportamento padrao
            
        document.formPrincipal.itNumeroGuiaSenha.focus();
            
    }
}

function eventoEnterNomeParaAcomodacao(campo,e){
        
    var n = $("input:text").length;
    if (e.which == 13) { //tecla Enter
        e.preventDefault(); //ignorar comportamento padrao
            
        document.formPrincipal.grauParticipacao.focus();
    }
}

function eventoEnterNomeParaDtProcedimento(campo,e){
        
    var n = $("input:text").length;
    if (e.which == 13) { //tecla Enter
        e.preventDefault(); //ignorar comportamento padrao
        var nextIndex = $('input:text').index(campo) + 3;
        if( n > nextIndex)
            $('input:text')[nextIndex].focus();
        else{
            $('input:text:first').focus();
        }
        ''
    }
}

function eventoEnterIndicacaoTipoATendimento(campo,e){
        
    var n = $("input:text").length;
    if (e.which == 13) { //tecla Enter
        e.preventDefault(); //ignorar comportamento padrao
            
        document.formPrincipal.tipoAtendimento.focus();
    }
}


function eventoEnterTipoATendimentoIndicacao(campo,e){
        
    var n = $("input:text").length;
    if (e.which == 13) { //tecla Enter
        e.preventDefault(); //ignorar comportamento padrao
            
        document.formPrincipal.indicacaoAcidente.focus();
    }
}


