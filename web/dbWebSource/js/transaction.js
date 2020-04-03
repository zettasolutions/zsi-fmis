  
var trans = (function(){
    
    var  bs    = zsi.bs.ctrl
        ,svn   = zsi.setValIfNull
        ,_public = {}
       
    ;
    
    zsi.ready = function(){
        $(".page-title").html("Transaction");
        displayTransactions(); 
    }; 
    function displayTransactions(){  
        var cb = app.bs({name:"cbFilter1",type:"checkbox"}); 
        $("#gridTransaction").dataBind({
                 sqlCode            : "T225" //transaction_sel 
                ,height             : $(window).height() - 235
                ,blankRowsLimit     : 5
                ,dataRows           : [
                        {text:cb        ,width:25              ,style : "text-align:left"
                            ,onRender  :  function(d){ return app.bs({name:"transaction_id"   ,type:"hidden"      ,value: svn (d,"transaction_id")}) 
                                            + app.bs({name:"is_edited"                  ,type:"hidden"      ,value: svn(d,"is_edited")}) 
                                            +  (d !==null ? app.bs({name:"cb",type:"checkbox"}) : "" );
                                            
                            }
                        
                        }
                        ,{text:"Transaction Date"                ,width:200       ,style:"text-align:left"
                            ,onRender   : function(d){ 
                                return app.bs({name:"transaction_date"     ,type:"input"      ,value: svn(d,"transaction_date").toShortDate()}); 
                            }
                        } 
                        ,{text:"Vehicle"                        ,type:"input"          ,name:"vehicle_id"                  ,width:150       ,style:"text-align:left"}
                        ,{text:"Route"                          ,type:"input"          ,name:"route_id"                    ,width:150       ,style:"text-align:left"}
                        ,{text:"From"                           ,type:"input"           ,name:"from_id"                     ,width:150       ,style:"text-align:left"}
                        ,{text:"To"                             ,type:"input"           ,name:"to_id"                       ,width:150       ,style:"text-align:left"} 
                        ,{text:"No of Regular"                  ,type:"input"           ,name:"no_regular"                  ,width:150       ,style:"text-align:left"}
                        ,{text:"No of Students"                 ,type:"input"           ,name:"no_students"                 ,width:150       ,style:"text-align:left"} 
                        ,{text:"No of SRCTZN"                   ,type:"input"           ,name:"no_sc"                       ,width:150       ,style:"text-align:left"}
                        ,{text:"No of PWD"                      ,type:"input"           ,name:"no_pwd"                      ,width:150       ,style:"text-align:left"} 
                        ,{text:"Paid Amount"                    ,type:"input"           ,name:"paid_amount"                 ,width:150       ,style:"text-align:left"}
                        ,{text:"Customer"                       ,type:"input"          ,name:"customer_id"                 ,width:150       ,style:"text-align:left"}
                        ,{text:"Payment Type"                   ,type:"input"          ,name:"payment_type"                ,width:150       ,style:"text-align:left"}
                        ,{text:"Payment Code"                   ,type:"input"          ,name:"payment_code"                ,width:150       ,style:"text-align:left"}
                        ,{text:"QR"                             ,type:"input"          ,name:"qr_id"                       ,width:150       ,style:"text-align:left"}
                        
                    ] 
                    ,onComplete : function(d){ 
                        var _zRow = this.find(".zRow");
                        this.find("[name='cbFilter1']").setCheckEvent("#gridTransaction input[name='cb']");    
                        _zRow.find("[name='transaction_date']").datepicker({pickTime  : false , autoclose : true });
                    } 
                });
            } 
         
         
        $("#btnSaveTransaction").click(function () { 
            $("#gridTransaction").jsonSubmit({
                 procedure: "transactions_upd" 
                ,onComplete: function (data) { 
                   if(data.isSuccess===true) zsi.form.showAlert("alert"); 
                    $("#gridTransaction").trigger("refresh");
                } 
            }); 
        });
        
       $("#btnDeleteTransaction").click(function (){ 
            zsi.form.deleteData({ 
                code:"ref-00016"
               ,onComplete:function(data){
                    displayTransactions();
               }
            });
        }); 
         
         
        return _public;
})();

















                     