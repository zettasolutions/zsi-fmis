var vehicleRepair = (function(){
    var  _public            = {}
        ,bs                 = zsi.bs.ctrl
        ,svn                = zsi.setValIfNull 
    ;
    zsi.ready = function(){
        $(".page-title").html("Vehicle Registration");
        $(".panel-container").css("min-height", $(window).height() - 160);   
        vehicleRegistration();
    };
    function selects(){
        $('#vehicle_id').select2({placeholder: "",allowClear: true});
        $('#paid_amount').maskMoney();
        $("#registration_date").datepicker({
              pickTime  : false
            , autoclose : true
            , todayHighlight: true 
        }).datepicker("setDate",new Date());
        $("#expiry_date").datepicker({
              pickTime  : false
            , autoclose : true
            , todayHighlight: true
        }).datepicker("setDate",new Date());
        
        $("#pms_type_id").dataBind({
            sqlCode      : "D235"
           ,text         : "pms_desc"
           ,value        : "pms_type_id"
        });
        
    }
    function vehicleRegistration(){
        var _cb = app.bs({name:"cbFilter1",type:"checkbox"});
        $("#gridVehicleRenewal").dataBind({
             sqlCode        : "V265"
            //,parameters     : {client_id: app.userInfo.company_id}
            ,blankRowsLimit : 5
            ,height         : $(window).height() - 480      
            ,dataRows       : [
                {text: _cb                                                                          ,width:25           ,style:"text-align:left"
                 ,onRender : function(d){
                         return app.bs({name:"vehicle_registration_id"               ,type:"hidden"      ,value: app.svn(d,"vehicle_registration_id")})  
                              + app.bs({name:"is_edited"                ,type:"hidden"      ,value: app.svn(d,"is_edited")})
                              + (d !== null ? app.bs({name:"cb"         ,type:"checkbox"}) : "" );
                     }
                 }
                ,{text: "Vehicle"                               ,name:"vehicle_id"              ,type:"select"      ,width: 120     ,style: "text-align:left;"}
                ,{text: "Registration No"                       ,name:"registration_no"         ,type:"input"       ,width: 120     ,style: "text-align:left;"}
                ,{text: "Registration Date"                     ,width: 120     ,style: "text-align:left;"
                     ,onRender : function(d){
                        return app.bs({name:"registration_date"       ,type:"input"          ,value: app.svn(d,"registration_date").toShortDate() })
                    }
                }
                ,{text: "Expiry Date"                           ,name:"expiry_date"             ,type:"input"       ,width: 120     ,style: "text-align:left;"
                    ,onRender : function(d){
                        return app.bs({name:"expiry_date"       ,type:"input"          ,value: app.svn(d,"expiry_date").toShortDate() })
                    }
                }
                ,{text: "Amount"                                ,name:"paid_amount"             ,type:"input"       ,width: 120     ,style: "text-align:left;"}
                
            ]
            ,onComplete: function(){
                var _this = this;
                 _this.find("[name='cbFilter1']").setCheckEvent("#gridVehicleRenewal input[name='cb']");
                _this.find("[name='vehicle_id']").dataBind({
                    sqlCode      : "D272"
                   ,parameters   : {client_id:app.userInfo.company_id}
                   ,text         : "vehicle_plate_no"
                   ,value        : "vehicle_id"
                });
                _this.find("[name='registration_date']").datepicker({
                     autoclose : true   
                    ,todayHighlight: true
                });
                _this.find("[name='expiry_date']").datepicker({
                     autoclose : true
                    ,todayHighlight: true
                });
            }
        });
    } 
    $("#delete").click(function () { 
        $("#gridVehicleRenewal").deleteData({
            sqlCode: "V265"
            ,parameters:{
                client_id:app.userInfo.company_id  
            }
            ,onComplete : function(data){
                 if(data.isSuccess===true) zsi.form.showAlert("alert");
                vehicleRegistration();
                //your code here .....
            }
         }) 
        /*$("#gridVehicleRenewal").deleteData({
            tableCode: "ref-00022"  
            ,onComplete: function (data) {
                if(data.isSuccess===true) zsi.form.showAlert("alert");
                vehicleRegistration();
            }
         }) */
    });
    $("#submit").click(function () { 
        $("#gridVehicleRenewal").jsonSubmit({
                 procedure: "vehicle_registration_upd"
                ,onComplete: function (data) {
                    if(data.isSuccess===true) zsi.form.showAlert("alert");
                    vehicleRegistration();
                }
        }); 
    }); 
      
    return _public;
    
    
    
})();       









               