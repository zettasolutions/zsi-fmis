   var pr=(function(){
        var      bs             = zsi.bs.ctrl
                ,svn            = zsi.setValIfNull 
                ,bsButton       = zsi.bs.button 
                ,_pub           = {} 
            ;
                
        zsi.ready = function(){
            $(".page-title").html("Part Replacements");
            displayPartReplacemet();
        };
         
        function displayPartReplacemet(){
            var cb = app.bs({name:"cbFilter1",type:"checkbox"}); 
            $("#gridPartReplacements").dataBind({
                     sqlCode    : "P249"
                    ,height     : $(window).height() - 235
                    ,blankRowsLimit : 5
                    ,dataRows   : [
                         {text: cb         ,width:25                   ,style:"text-align:left"
                             ,onRender : function(d){
                                 return app.bs({name:"replacement_id"                           ,type:"hidden"      ,value: svn (d,"replacement_id")}) 
                                    +   bs({name:"is_edited"                    ,type:"hidden"              ,value: svn (d,"is_edited")})
                                    + (d !== null ? app.bs({name:"cb"           ,type:"checkbox"}) : "" );
                             }
                         }
                       /* ,{text:"Replacement Date"      ,width:120          ,style:"text-align:left"
                            ,onRender: function(d){  
                                return app.bs({name:"replacement_date"                  ,type:"input"      ,value: svn (d,"replacement_date").toShortDate() });  
                            }
                        }*/
                        //,{text:"Vehicle"            ,type:"select"       ,name:"vehicle_id"          ,width:150          ,style:"text-align:left"}
                        ,{text:"Part"               ,type:"select"       ,name:"part_id"             ,width:150          ,style:"text-align:left"}
                        ,{text:"Part QTY"           ,type:"input"        ,name:"part_qty"            ,width:60          ,style:"text-align:center"}
                        ,{text:"Unit"               ,type:"select"       ,name:"unit_id"             ,width:120          ,style:"text-align:left"}
                      ]
                      ,onComplete : function(){
                        var _this = this;
                        _this.find("[name='cbFilter1']").setCheckEvent("#gridPartReplacements input[name='cb']"); 
                        _this.find("input[name='replacement_date']").datepicker({ 
                              pickTime  : false
                            , autoclose : true
                            , todayHighlight: true 
                        });
                        _this.find("select[name='vehicle_id']").dataBind({
                            sqlCode      : "D231"
                           ,text         : "plate_no"
                           ,value        : "vehicle_id" 
                        });
                        _this.find("select[name='part_id']").dataBind({
                            sqlCode      : "D256"
                           ,text         : "part_desc"
                           ,value        : "part_id" 
                        });
                        _this.find("select[name='unit_id']").dataBind({
                            sqlCode      : "D257"
                           ,text         : "unit_name"
                           ,value        : "unit_id" 
                        });
                    }
            });
        
        }       
        
        $("#btnSave").click(function (){
            $("#gridPartReplacements").jsonSubmit({
                procedure:"part_replacements_upd"
                ,onComplete:function(data){ 
                    if(data.isSuccess===true)zsi.form.showAlert("alert");
                    displayPartReplacemet();
                }
            });
        }); 
        $("#btnDelete").click(function (){
            console.log("inside delete");
            zsi.form.deleteData({ 
                    code:"ref-00018"
                   ,onComplete:function(data){
                        displayPartReplacemet();
                   }
            });
        });  
    return _pub;
})();           
           
           
           
                                                                                                                    
                                                                                                                                                                                                                                                                         