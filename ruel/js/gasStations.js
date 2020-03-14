var stations = (function(){
    
    var  bs             = zsi.bs.ctrl
        ,svn            = zsi.setValIfNull
        ,mdlInactive    = "modalWindowInactive"
        ,gTw            = null
        ,pub            = {}
    ;
    
    zsi.ready = function(){
        $(".page-title").html("Gas Stations");
        gTw = new zsi.easyJsTemplateWriter();
        displayStations();
        getTemplates();
    };
    
    function getTemplates(){
        new zsi.easyJsTemplateWriter("body")
        .bsModalBox({
              id        : mdlInactive
            , sizeAttr  : "modal-lg"
            , title     : "Inactive Gas Stations"
            , body      : gTw.new().modalBodyInactive({grid:"gridInactiveStations",onClickSaveInactive:"stations.submitInactive();" ,onClickDeleteInactive:"stations.deleteInactive();"}).html()  
        });
    }

    function displayStations(){
        var cb = app.bs({name:"cbFilter1",type:"checkbox"});
        $("#gridStation").dataBind({
             sqlCode    : "G215" //gas_stations_sel
            ,width      : $(".zContainer").outerWidth()
            ,height     : $(window).height() - 235
            ,blankRowsLimit : 5
            ,dataRows   : [
                 {text: cb                      ,width:25           ,style:"text-align:center"
                     ,onRender : function(d){
                        return bs({name:"gas_station_id"        ,type:"hidden"  ,value: svn (d,"gas_station_id")})
                             + bs({name:"is_edited"             ,type:"hidden"  ,value: svn(d,"is_edited")}) 
                             + (d !== null ? bs({name:"cb"  ,type:"checkbox"}) : "" );
                     }
                 }
                ,{text:"Code"            ,type:"input"       ,name:"gas_station_code"    ,width:85         ,style:"text-align:left"}
                ,{text:"Name"            ,type:"input"       ,name:"gas_station_name"    ,width:200        ,style:"text-align:left"  }
                ,{text:"Address"         ,type:"input"       ,name:"gas_station_addr"    ,width:250        ,style:"text-align:left"  }
                ,{text:"Active?"         ,type:"yesno"       ,name:"is_active"           ,width:60         ,style:"text-align:center"    ,defaultValue:"Y"}
            ]
            ,onComplete : function(o){
                this.find("[name='cbFilter1']").setCheckEvent("#gridStation input[name='cb']");
            }
            
        });
    }  

    function displayInactiveStations(){
        var cb = app.bs({name:"cbFilter1",type:"checkbox"});
        $("#gridInactiveStations").dataBind({
             sqlCode    : "G215" //gas_stations_sel
            ,parameters : {is_active: "N"}
            ,width      : $(".zContainer").outerWidth()
            ,height     : $(document).height() - 260
            ,dataRows   : [
                 {text: cb                      ,width:25           ,style:"text-align:center"
                     ,onRender : function(d){
                        return bs({name:"gas_station_id"        ,type:"hidden"  ,value: svn (d,"gas_station_id")})
                             + bs({name:"is_edited"             ,type:"hidden"  ,value: svn(d,"is_edited")}) 
                             + (d !== null ? bs({name:"cb"  ,type:"checkbox"}) : "" );
                     }
                 }
                ,{text:"Code"            ,width:85         ,style:"text-align:left"
                    ,onRender : function(d){
                        return app.svn(d,"gas_station_code")
                             + bs({name:"gas_station_code"  ,type:"hidden"  ,value:app.svn(d,"gas_station_code")})

                    }
                    
                }
                ,{text:"Name"            ,width:200         ,style:"text-align:left"   
                    ,onRender : function(d){
                        return app.svn(d,"gas_station_name")
                             + bs({name:"gas_station_name"  ,type:"hidden"  ,value:app.svn(d,"gas_station_name")})
                             + bs({name:"gas_station_addr"  ,type:"hidden"  ,value:app.svn(d,"gas_station_addr")});
                             
                    }
                }
                ,{text:"Active?"         ,type:"yesno"       ,name:"is_active"           ,width:50         ,style:"text-align:center"    ,defaultValue:"Y"}
            ]
            ,onComplete : function(o){
                this.find("[name='cbFilter1']").setCheckEvent("#gridInactiveStations input[name='cb']");
            }
        });
    }  

   pub.submitInactive = function(){
        var _$grid = $("#gridInactiveStations");
            _$grid.jsonSubmit({
                 procedure: "gas_stations_upd"
                ,optionalItems: ["is_active"]
                ,onComplete: function (data) {
                    if(data.isSuccess===true) zsi.form.showAlert("alert");
                    displayStations();
                    displayInactiveStations();
                }
            });
        $('#' + mdlInactive).modal('hide');     
    }; 

    pub.deleteInactive = function(){
        zsi.form.deleteData({
             code       : "ref-00013"
            ,onComplete : function(data){
                displayInactiveStations();
                $('#' + mdlInactive).modal('hide');
            }
        });    
    }; 
        
    $("#btnSave").click(function () {
       $("#gridStation").jsonSubmit({
                 procedure: "gas_stations_upd"
                ,onComplete: function (data) {
                    if(data.isSuccess===true) zsi.form.showAlert("alert");
                    displayStations();
                }
        });
    });
    
    $("#btnInactive").click(function () {
        var g$mdl = $("#" + mdlInactive);
        g$mdl.find(".modal-title").text("Inactive Gas Stations") ;
        g$mdl.modal({ show: true, keyboard: false, backdrop: 'static' });
        displayInactiveStations();
    });
    
    $("#btnDelete").click(function(){
        zsi.form.deleteData({
             code       : "ref-00013"
            ,onComplete : function(data){
                displayStations();
            }
        });   
    });  
    
    return pub;
})();

                     