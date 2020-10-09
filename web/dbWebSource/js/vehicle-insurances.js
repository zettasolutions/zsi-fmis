var accidents = (function(){
    var  _public            = {}
        ,bs                 = zsi.bs.ctrl
        ,svn                = zsi.setValIfNull 
    ;
    zsi.ready = function(){
        $(".page-title").html("Vehicle Insurance");
        $(".panel-container").css("min-height", $(window).height() - 160); 
        displayInsurances();
        
    }; 
    function displayInsurances(){  
        var cb = app.bs({name:"cbFilter1",type:"checkbox"}); 
        $("#gridInsurances").dataBind({
             sqlCode        : "V268"
            ,height         : $(window).height() - 273 
            ,blankRowsLimit : 5
            ,dataRows   : [
                     {text: cb                                                                          ,width:25           ,style:"text-align:left"
                         ,onRender : function(d){
                             return app.bs({name:"vehicle_insurance_id"         ,type:"hidden"              ,value: app.svn(d,"vehicle_insurance_id")}) 
                                  + app.bs({name:"is_edited"                    ,type:"hidden"              ,value: app.svn(d,"is_edited")})
                                  + (d !== null ? app.bs({name:"cb"             ,type:"checkbox"}) : "" );
                         }
                     }
                    ,{text:"Vehicle"                    ,type:"select"          ,name:"vehicle_id"                      ,width:120          ,style:"text-align:left"}
                    ,{text:"Insurance No"               ,type:"input"           ,name:"insurance_no"                    ,width:120          ,style:"text-align:left"}
                    ,{text:"Insurance Date"             ,width:120              ,style:"text-align:left"
                        ,onRender : function(d){
                            return app.bs({name:"insurance_date"                ,type:"input"              ,value: app.svn(d,"insurance_date").toShortDate()}) ; 
                        }
                    }
                    ,{text:"Insurance Company"          ,type:"input"           ,name:"insurance_company_id"            ,width:120          ,style:"text-align:left"}
                    ,{text:"Expiry Date"                ,width:120          ,style:"text-align:left"
                        ,onRender : function(d){
                            return app.bs({name:"expiry_date"                ,type:"input"              ,value: app.svn(d,"expiry_date").toShortDate()}) ; 
                        }
                    }
                    ,{text:"Insurance Type"             ,type:"input"           ,name:"insurance_type_id"               ,width:120          ,style:"text-align:left"}
                    ,{text:"Insured Amount"                                                                             ,width:120          ,style:"text-align:right"
                        ,onRender: function(d){
                            return app.bs({name:"insured_amount"       ,type:"input"              ,value: commaSeparateNumber(app.svn(d,"insured_amount"))       ,style : "text-align:right"});
                        }
                    }
                    ,{text:"Paid Amount"                                                                                ,width:120          ,style:"text-align:right"
                        ,onRender: function(d){
                            return app.bs({name:"paid_amount"          ,type:"input"              ,value: commaSeparateNumber(app.svn(d,"paid_amount"))       ,style : "text-align:right"})
                                 + app.bs({type: "hidden", name: "is_posted"});
                        }
                    } 
                ]
                     
              ,onComplete : function(o){
                var _dRows = o.data.rows;
                var _this  = this;
    	        var _zRow  = _this.find(".zRow"); 
    	        _this.find("[name='insured_amount'],[name='paid_amount']").maskMoney();
    	        _zRow.find("[name='vehicle_id']").dataBind({
                    sqlCode      : "D272"
                   ,parameters   : {client_id:app.userInfo.company_id}
                   ,text         : "vehicle_plate_no"
                   ,value        : "vehicle_id"
                });
                _zRow.find("[name='insurance_date'],[name='expiry_date']").datepicker({
                    autoclose: true
                   ,todayHighlight: true
                });
                this.find("[name='cbFilter1']").setCheckEvent("#gridInsurances input[name='cb']");
              } 
        });
    } 
        
    function commaSeparateNumber(n){
        var _res = "";
        if($.isNumeric(n)){
            var _num = parseFloat(n).toFixed(2).toString().split(".");
            _res = _num[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",") + (!isUD(_num[1]) ? "." + _num[1] : "");
        }
        return _res;
    }
     
    
    $("#btnDeleteInsurance").click(function () {
         zsi.form.deleteData({ 
                code:"ref-00021" 
               ,onComplete:function(data){
                     displayInsurances();
               }
        }); 
    });
    
    $("#btnSave").click(function () {
        var _$grid = $("#gridInsurances");
        var _$insureAmt = _$grid.find("input[name='insured_amount']");
        var _$paidAmt = _$grid.find("input[name='paid_amount']");
            _$insureAmt.each(function(){
                this.value = this.value.replace(/,/g, "");
            });
            _$paidAmt.each(function(){
                this.value = this.value.replace(/,/g, "");
            });
        _$grid.jsonSubmit({
             procedure: "vehicle_insurances_upd" 
            ,onComplete: function (data) {
                if(data.isSuccess){
                    console.log("data",data);
                   if(data.isSuccess===true) zsi.form.showAlert("alert"); 
                   $("#gridInsurances").trigger("refresh");
                } 
            }
        }); 
    });
    
    $("#btnPost").click(function () {
        var _$grid = $("#gridInsurances");
        var _$insureAmt = _$grid.find("input[name='insured_amount']");
        var _$paidAmt = _$grid.find("input[name='paid_amount']");
            _$insureAmt.each(function(){
                this.value = this.value.replace(/,/g, "");
            });
            _$paidAmt.each(function(){
                this.value = this.value.replace(/,/g, "");
            });
        _$grid.find('input:checked').each(function() {
                   //$(this).find(".zRow").find("[name='is_posted']").val("Y");
                   $(this).closest(".zRow").find("[name='is_posted']").val("Y");
            });       
        _$grid.jsonSubmit({
             procedure: "vehicle_insurances_upd" 
            ,onComplete: function (data) {
                if(data.isSuccess){
                    console.log("data",data);
                   if(data.isSuccess===true) zsi.form.showAlert("alert"); 
                   $("#gridInsurances").trigger("refresh");
                } 
            }
        }); 
    }); 
    
    return _public;
    
    
    
})();                    