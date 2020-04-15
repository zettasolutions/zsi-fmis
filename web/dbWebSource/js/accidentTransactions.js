  
var accident = (function(){
    
    var  bs    = zsi.bs.ctrl
        ,svn   = zsi.setValIfNull
        ,_public = {}
       
    ;
    
    zsi.ready = function(){
        $(".page-title").html("Accident Transactions");
        displayAccidentTransactions(); 
    }; 
    function displayAccidentTransactions(){  
        var cb = app.bs({name:"cbFilter1",type:"checkbox"}); 
        $("#gridAccident").dataBind({
                 sqlCode            : "A221" //accident_transactions_sel 
                ,height             : $(window).height() - 235
                ,blankRowsLimit     : 5
                ,dataRows           : [
                        {text:cb        ,width:25              ,style : "text-align:left"
                            ,onRender  :  function(d){ return app.bs({name:"accident_id"   ,type:"hidden"      ,value: svn (d,"accident_id")}) 
                                            + app.bs({name:"is_edited"                  ,type:"hidden"      ,value: svn(d,"is_edited")}) 
                                            +  (d !==null ? app.bs({name:"cb",type:"checkbox"}) : "" );
                                            
                            }
                        
                        }    
                        ,{text:"Accident Date"                       ,width:100       ,style:"text-align:left"
                            ,onRender   : function(d){ 
                                return app.bs({name:"accident_date"     ,type:"input"      ,value: svn(d,"accident_date").toShortDate()}); 
                            }
                        } 
                        ,{text:"Vehicle"                        ,type:"select"           ,name:"vehicle_id"                 ,width:150       ,style:"text-align:left"}
                        ,{text:"Driver"                         ,type:"select"           ,name:"driver_id"                  ,width:150       ,style:"text-align:left"}
                        ,{text:"Pao"                            ,type:"select"           ,name:"pao_id"                     ,width:150       ,style:"text-align:left"}
                        ,{text:"Accident Type"                  ,type:"input"            ,name:"accident_type_id"           ,width:150       ,style:"text-align:left"}
                        ,{text:"Accident Level"                 ,type:"input"            ,name:"accident_level"             ,width:150       ,style:"text-align:left"}
                        ,{text:"Error Type"                     ,type:"input"            ,name:"error_type_id"              ,width:150       ,style:"text-align:left"}
                        ,{text:"Comments"                       ,type:"input"            ,name:"comments"                   ,width:150       ,style:"text-align:left"} 
                    ] 
                    ,onComplete : function(d){ 
                        var _zRow = this.find(".zRow");
                        this.find("[name='cbFilter1']").setCheckEvent("#gridAccident input[name='cb']");   
                        _zRow.find("[name='accident_date']").datepicker({pickTime  : false , autoclose : true , todayHighlight: true}); 
                        _zRow.find("[name='vehicle_id']").dataBind({
                             sqlCode    : "D231" // dd_vehicle_sel
                            ,text   : "plate_no"
                            ,value  : "vehicle_id"
                        });
                        _zRow.find("[name='driver_id']").dataBind({
                             sqlCode    : "D227" // drivers_sel
                            ,text   : "full_name"
                            ,value  : "user_id"
                        });
                        _zRow.find("[name='pao_id']").dataBind({
                             sqlCode    : "P228" // pao_sel
                            ,text   : "full_name"
                            ,value  : "user_id"
                        });
                         
                    } 
                });
            }   
    
    
        $("#btnSaveAccidents").click(function () { 
            $("#gridAccident").jsonSubmit({
                 procedure: "accident_transactions_upd" 
                ,onComplete: function (data) { 
                   if(data.isSuccess===true) zsi.form.showAlert("alert"); 
                    $("#gridAccident").trigger("refresh");
                } 
            }); 
        });
        
       $("#btnDeleteAccidents").click(function (){ 
            zsi.form.deleteData({ 
                code:"ref-00015"
               ,onComplete:function(data){
                    displayAccidentTransactions();
               }
            });
        });
    
})();

                      