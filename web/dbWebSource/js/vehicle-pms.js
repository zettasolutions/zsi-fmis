  var pms = (function(){
    var  _pub = {}
        ,gPMSid  = ""
    ;
    zsi.ready = function(){
        $(".page-title").html("Vehicle PMS");
        $(".panel-container").css("min-height", $(window).height() - 160);
        //validations();
        //$("#client_phone_no").inputmask({"mask": "(99) 9999 - 9999"});
        $("#pms_date").datepicker({todayHighlight:true}).datepicker("setDate",new Date());
        $("#pms_type_id").dataBind({
            sqlCode      : "D235" //dd_pms_type_sel
           ,text         : "pms_desc"
           ,value        : "pms_type_id"
           ,onChange     : function(d){
                var _info           = d.data[d.index - 1]
                    ,pms_type_id     = isUD(_info) ? "" : _info.pms_type_id;
           }
        });
        $("#vehicle_id").dataBind({
            sqlCode      : "D231" //dd_vehicle_sel
           ,text         : "plate_no"
           ,value        : "vehicle_id"
           ,onChange     : function(d){
                var _info           = d.data[d.index - 1]
                    ,vehicle_id     = isUD(_info) ? "" : _info.vehicle_id;
           }
        });
        $("#status_id").dataBind({
            sqlCode      : "S122" //statuses_sel
           ,text         : "status_name"
           ,value        : "status_id"
           ,onChange     : function(d){
                var _info           = d.data[d.index - 1]
                    ,status_id     = isUD(_info) ? "" : _info.status_id; 
           }
        });
        
        dispalyReplacementParts();
    };
    
    function dispalyReplacementParts(){
        var _dataBind = {
            height : $("#divReplacementParts").closest(".panel-container").height()
            ,blankRowsLimit : 10
            ,dataRows : [
                {text: "Item No.", width: 60
                    ,onRender : function(d){ 
                        return app.bs({type: "hidden", name: "replacement_id", value: app.svn(d,"replacement_id")})
                            +  app.bs({type: "hidden", name: "is_edited"})  
                            +  app.bs({type: "hidden", name: "pms_id", value: gPMSid})  
                            +  app.bs({type: "hidden", name: "repair_id"})  
                            +  app.bs({type: "input",  name: "seq_no", value: app.svn(d,"seq_no"), style: "text-align:center"}); 
                    }
                }
                ,{text: "Part", type: "select", name: "part_id", width: 130, style: "text-align:left"}
                ,{text: "Part Qty", width: 120
                    ,onRender : function(d){ 
                        return app.bs({type: "input",  name: "part_qty", value: commaSeparateNumber(app.svn(d,"part_qty")), style: "text-align:center"});
                    }
                }
                ,{text: "Unit", type: "select", name: "unit_id", width: 130, style: "text-align:left"}
                ,{text: "Unit Cost", width: 120
                    ,onRender : function(d){ 
                        return app.bs({type: "input",  name: "unit_cost", value: commaSeparateNumber(app.svn(d,"unit_cost")), style: "text-align:center"});
                    }
                }
                ,{text: "Total Cost", width: 120
                    ,onRender : function(d){ 
                        return app.bs({type: "input",  name: "total_cost", value: commaSeparateNumber(app.svn(d,"total_cost")), style: "text-align:center"});
                    }
                }
                ,{text: "Replaced (Yes/No)", type: "yesno", name: "is_replacement", width: 120, style: "text-align:center", defaultValue:"N"}
                ,{text: "New (Yes/No)", type: "yesno", name: "is_bnew", width: 100, style: "text-align:center", defaultValue:"N"}
            ]
            ,onComplete : function(o){
                var _$this = this;
                _$this.find("[name='part_id']").dataBind({
                    sqlCode      : "D256" //dd_parts_sel
                   ,text         : "part_desc"
                   ,value        : "part_id"
                   ,onChange     : function(d){
                    //   var _info = d.data[d.index - 1];
                    //   part_id = isUD(_info) ? "" : _info.part_id; 
                   }
                });
                _$this.find("[name='unit_id']").dataBind({
                    sqlCode      : "D257" //dd_units_sel
                   ,text         : "unit_name"
                   ,value        : "unit_id"
                   ,onChange     : function(d){
                    //   var _info = d.data[d.index - 1];
                    //   unit_id = isUD(_info) ? "" : _info.unit_id; 
                   }
                });
                
                _$this.find("[name='part_qty'],[name='unit_cost']").focusout(function(){
                    var _$row = $(this).closest(".zRow");
                    var _$totalCost = _$row.find("[name='total_cost']")
                        ,_$partQty = _$row.find("[name='part_qty']")
                        ,_$unitCost = _$row.find("[name='unit_cost']")
                        ,_partQty = _$partQty.val().replace(/,/g, "")
                        ,_unitCost = _$unitCost.val().replace(/,/g, "")
                        ,_totalCost = "";
                        
                        if(_partQty!=="" && _unitCost!==""){
                            _totalCost = parseFloat(_partQty).toFixed(2) * parseFloat(_unitCost).toFixed(2);
                            _$totalCost.val(commaSeparateNumber(_totalCost));
                        }else{
                            _$totalCost.val("");
                        }
                });
                
                _$this.find("[name='total_cost']").attr("disabled",true);
                _$this.find("[name='part_qty'],[name='unit_cost']").addClass("numeric");
                zsi.initInputTypesAndFormats();
            }
        };
        
        if(gPMSid!==""){
            _dataBind.sqlCode = "P249"; //part_replacements_sel
            _dataBind.parameters = {pms_id: gPMSid};
        }
        
        $("#gridReplacementParts").dataBind(_dataBind);
    }
    
    function modalTxt(){
        setTimeout(function(){
           $("#myModal").find("#msg").text("Are you sure you want to save this data?");
           $("#myModal").find("#msg").css("color","#000");
        },1000);
    }
    
    function commaSeparateNumber(n){
        var _res = "";
        if($.isNumeric(n)){
            var _num = parseFloat(n).toFixed(2).toString().split(".");
            _res = _num[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",") + (!isUD(_num[1]) ? "." + _num[1] : "");
        }
        return _res;
    }
    
    function validations(){
        var forms = document.getElementsByClassName('needs-validation');
    	// Loop over them and prevent submission
    	var validation = Array.prototype.filter.call(forms, function(form) {
    		form.addEventListener('submit', function(event) {
    			if (form.checkValidity() === false) {
    				event.preventDefault();
    				event.stopPropagation();
    			    $("form").addClass('was-validated');
    			}else{
        			event.preventDefault();
        			event.stopPropagation();
    			    $('#myModal').modal('show');
    			    $("form").addClass('was-validated');
    			}
    		}, false);
    	});
    }
    
    function submitReplacementParts(){
        var _$frm = $("#formPMS")
            ,_$grid = $("#gridReplacementParts");
        
        _$frm.find("[name='pms_id']").val(gPMSid);
        _$grid.find("[name='part_qty'], [name='unit_cost']").each(function(){
            this.value = this.value.replace(/,/g, "");
        });
        _$grid.jsonSubmit({
             procedure: "part_replacements_upd" 
             ,notIncludes: ["total_cost"]
             ,onComplete: function(data){
                if(data.isSuccess){
                    zsi.form.showAlert("alert"); 
                    
                    $("#btnNew").removeClass("hide");
                    dispalyReplacementParts();
                }
             }
        });
    }
    
    $("#btnNew").click(function () {
        var _params = ['#p','vehicle-pms'].join("/");
        window.open(_params,"_self");    
    });
    
    $("#btnSave").click(function () {
        var _$div = $("#divVehiclePMS") 
            ,_date = $("#pms_date").val()
            ,_pmsType = $("#pms_type_id").val()
            ,_vehicle = $("#vehicle_id").val()
            ,_odoReading = $("#odo_reading").val()
            ,_amount = $("#pm_amount").val().replace(/,/g, "")
            ,_location = $("#pm_location").val()
            ,_comment = $("#comment").val()
            ,_status = $("#status_id").val();
        
        $.post(app.procURL + "vehicle_pms_upd @pms_id="+ gPMSid +",@pms_date='"+ _date +"',@pms_type_id="+ _pmsType +",@vehicle_id="+ _vehicle +",@odo_reading="+ _odoReading
                            +",@pm_amount="+ _amount +",@pm_location='" +_location +"',@comment='"+ _comment +"',@status_id="+ _status
            ,function(data){
                if(data.isSuccess){
                    gPMSid = data.returnValue;
                    submitReplacementParts();
                }      
        });
    });     
    
    // $("#btnSavePMS").click(function () {
    //     $("#formPMS").jsonSubmit({
    //          procedure: "vehicle_pms_upd"
    //         ,isSingleEntry: true
    //         ,onComplete: function (data) {
    //             if(data.isSuccess){
    //               if(data.isSuccess===true) zsi.form.showAlert("alert");
    //               $("form").removeClass('was-validated');
    //               $("#formPMS").find("input").val("");
    //               $("#formPMS").find("textarea").val("");
    //               $("#formPMS").find("select").val(null).trigger('change');
    //               $("#myModal").find("#msg").text("Data successfully saved.");
    //               $("#myModal").find("#msg").css("color","green");
    //               setTimeout(function(){
    //                   $("#myModal").modal('toggle');
    //                   $("#pms_date").datepicker({todayHighlight:true}).datepicker("setDate",new Date());
    //                   modalTxt();
    //               },1000);
    //             }else{
    //               $("#myModal").find("#msg").text("Something went wrong when saving the data.");
    //               $("#myModal").find("#msg").css("color","red");
    //             }
    //         }
    //     }); 
    // });
    
    return _pub;
})();              