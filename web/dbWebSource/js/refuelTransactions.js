 
var refuel = (function(){
    
    var  bs    = zsi.bs.ctrl
        ,svn   = zsi.setValIfNull
        ,_public = {}
       
    ;
    
    zsi.ready = function(){
        $(".page-title").html("Refuel Transactions");
        displayRefuelTransactions(); 
    }; 
    function displayRefuelTransactions(){  
        var cb = app.bs({name:"cbFilter1",type:"checkbox"}); 
        $("#gridRefuel").dataBind({
                 sqlCode            : "R216" //refuel_transactions_sel 
                ,height             : $(window).height() - 235
                ,blankRowsLimit     : 5
                ,dataRows           : [
                        {text:cb        ,width:25              ,style : "text-align:left"
                            ,onRender  :  function(d){ return app.bs({name:"refuel_id"   ,type:"hidden"      ,value: app.svn(d,"refuel_id")}) 
                                            + app.bs({name:"is_edited"                  ,type:"hidden"      ,value: app.svn(d,"is_edited")}) 
                                            +  (d !==null ? app.bs({name:"cb",type:"checkbox"}) : "" );
                                            
                            }
                        
                        }   
                        ,{text:"Doc No"                         ,type:"input"          ,name:"doc_no"                    ,width:100       ,style:"text-align:left"}
                        ,{text:"Doc Date"                       ,width:100       ,style:"text-align:left"
                            ,onRender   : function(d){ 
                                return app.bs({name:"doc_date"     ,type:"input"      ,value: svn(d,"doc_date").toShortDate()}); 
                            }
                        } 
                        ,{text:"Vehicle"                        ,type:"select"          ,name:"vehicle_id"                ,width:110       ,style:"text-align:left"}
                        ,{text:"Driver"                         ,type:"select"          ,name:"driver_id"                 ,width:150       ,style:"text-align:left"}
                        ,{text:"Pao"                            ,type:"select"          ,name:"pao_id"                    ,width:150       ,style:"text-align:left"}
                        ,{text:"Odo Reading"                    ,type:"input"           ,name:"odo_reading"               ,width:150       ,style:"text-align:left"}
                        ,{text:"Gas Station"                    ,type:"select"          ,name:"gas_station_id"            ,width:100       ,style:"text-align:left"}
                        ,{text:"Number of Liters"                                                                         ,width:100       ,style:"text-align:center"
                            ,onRender: function(d){
                                return app.bs({name: "no_liters"             ,type: "input"     ,value: app.svn(d,"no_liters")         ,style : "text-align:center;"});
                            }
                        }
                        ,{text:"Unit Price"                                                                              ,width:70        ,style:"text-align:right;padding-right: 0.3rem;"
                            ,onRender: function(d){
                                return app.bs({name: "unit_price"             ,type: "input"     ,value: app.svn(d,"unit_price") !=="" ? app.svn(d,"unit_price").toMoney() : app.svn(d,"unit_price")         ,style : "text-align:right;padding-right: 0.3rem;"});
                            }
                        }
                        ,{text:"Refuel Amount"                                                                           ,width:90       ,style:"text-align:right;padding-right: 0.3rem;"
                            ,onRender: function(d){
                                return app.bs({name: "refuel_amount"          ,type: "input"     ,value: app.svn(d,"refuel_amount") !=="" ? app.svn(d,"refuel_amount").toMoney() : app.svn(d,"refuel_amount")      ,style : "text-align:right;padding-right: 0.3rem;"});
                            }
                        }
                        /*,{text:"Is Posted"                      ,type:"yesno"          ,name:"is_posted"                 ,width:70       ,style:"text-align:left" ,defaultValue:"N"} 
                        ,{text:"Posted Date"                    ,width:100       ,style:"text-align:left"
                            ,onRender   : function(d){ 
                                return app.bs({name:"posted_date"     ,type:"input"      ,value: svn(d,"posted_date").toShortDate()}); 
                            }
                        }*/
                        
                    ] 
                    ,onComplete : function(d){ 
                        var _zRow = this.find(".zRow");
                        /*_zRow.find("input").prop('required',true);*/
                        this.find("[name='cbFilter1']").setCheckEvent("#gridRefuel input[name='cb']");   
                        _zRow.find("[name='posted_date']").datepicker({pickTime  : false , autoclose : true , todayHighlight: true});
                        _zRow.find("[name='doc_date']").datepicker({pickTime  : false, autoclose : true, todayHighlight: true});
                        _zRow.find("[name='gas_station_id']").dataBind({
                             sqlCode    : "G215" // gas_station_sel
                            ,text   : "gas_station_name"
                            ,value  : "gas_station_id"
                        });
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
    
    
        $("#btnSaveRefuel").click(function () { 
            $("#gridRefuel").jsonSubmit({
                 procedure: "refuel_transactions_upd"
                //,optionalItems: ["is_active"] 
                ,onComplete: function (data) { 
                   if(data.isSuccess===true) zsi.form.showAlert("alert"); 
                    $("#gridRefuel").trigger("refresh");
                } 
            }); 
        });
        
       $("#btnDeleteRefuel").click(function (){ 
            zsi.form.deleteData({ 
                code:"ref-00014"
               ,onComplete:function(data){
                    displayRefuelTransactions();
               }
            });
        });
    
})();

                         