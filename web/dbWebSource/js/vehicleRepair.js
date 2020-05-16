 
  var vehicleRepair = (function(){
    var  _public = {}
        ,gRepairId = "" 
    ;
    zsi.ready = function(){
        $(".page-title").html("Vehicle Repair");
        $(".panel-container").css("min-height", $(window).height() - 160);   
        //validations();
        dispalyReplacementParts();
        selects();
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
                            +  app.bs({type: "hidden", name: "pms_id"})  
                            +  app.bs({type: "hidden", name: "repair_id", value: gRepairId})  
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
        
        if(gRepairId!==""){
            _dataBind.sqlCode = "P249"; //part_replacements_sel
            _dataBind.parameters = {repair_id: gRepairId};
        }
        
        $("#gridReplacementParts").dataBind(_dataBind);
    }
    
    function commaSeparateNumber(n){
        var _res = "";
        if($.isNumeric(n)){
            var _num = parseFloat(n).toFixed(2).toString().split(".");
            _res = _num[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",") + (!isUD(_num[1]) ? "." + _num[1] : "");
        }
        return _res;
    }
    
    function selects(){
        $("#repair_date").datepicker({todayHighlight:true}).datepicker("setDate",new Date());
        $("#pms_type_id").dataBind({
            sqlCode      : "D235" //dd_pms_type_sel
           ,text         : "pms_desc"
           ,value        : "pms_type_id"
           ,onChange     : function(d){
               var _info           = d.data[d.index - 1];
               pms_type_id     = isUD(_info) ? "" : _info.pms_type_id; 
           }
        });
        $("#vehicle_id").dataBind({
            sqlCode      : "D231" //dd_vehicle_sel
           ,text         : "plate_no"
           ,value        : "vehicle_id"
           ,onChange     : function(d){
               var _info           = d.data[d.index - 1];
                vehicle_id     = isUD(_info) ? "" : _info.vehicle_id; 
           }
        });
        $("#status_id").dataBind({
            sqlCode      : "S122" //statuses_sel
           ,text         : "status_name"
           ,value        : "status_id"
           ,onChange     : function(d){
               var _info           = d.data[d.index - 1];
                status_id     = isUD(_info) ? "" : _info.status_id; 
           }
        });
    }
    
    function validations(){ 
        var forms = document.getElementsByClassName('needs-validation'); 
    	var validation = Array.prototype.filter.call(forms, function(form) {
    		form.addEventListener('submit', function(event) {
    		    $("form").removeClass('was-validated');
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
    // FOR ODO READING INPUT TYPE.
    function setInputFilter(textbox, inputFilter) {
      ["input", "keydown"].forEach(function(event) {
        textbox.addEventListener(event, function() {
          if (inputFilter(this.value)) {
            this.oldValue = this.value;
            this.oldSelectionStart = this.selectionStart;
            this.oldSelectionEnd = this.selectionEnd;
          } else if (this.hasOwnProperty("oldValue")) {
            this.value = this.oldValue;
            this.setSelectionRange(this.oldSelectionStart, this.oldSelectionEnd);
          } else {
            this.value = "";
          }
        });
      });
    } 
    setInputFilter(document.getElementById("odo_reading"), function(value) {
      return /^-?\d*$/.test(value);  
    }); 
    
    function submitReplacementParts(){
        var _$frm = $("#formVehicleRepair")
            ,_$grid = $("#gridReplacementParts");
        
        _$frm.find("[name='repair_id']").val(gRepairId);
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
        var _params = ['#p','vehicleRepair'].join("/");
        window.open(_params,"_self");    
    });
    
    $("#btnSave").click(function () {
        var _$div = $("#divVehicleRepair") 
            ,_date = $("#repair_date").val()
            ,_pmsType = $("#pms_type_id").val()
            ,_vehicle = $("#vehicle_id").val()
            ,_odoReading = $("#odo_reading").val()
            ,_amount = $("#repair_amount").val().replace(/,/g, "")
            ,_location = $("#repair_location").val()
            ,_comment = $("#comment").val()
            ,_status = $("#status_id").val();
            
        $.post(app.procURL + "vehicle_repairs_upd @repair_id="+ gRepairId +",@repair_date='"+ _date +"',@pms_type_id="+ _pmsType +",@vehicle_id="+ _vehicle +",@odo_reading="+ _odoReading
                            +",@repair_amount="+ _amount +",@repair_location='" +_location +"',@comment='"+ _comment +"',@status_id="+ _status
            ,function(data){
                if(data.isSuccess){
                    gRepairId = data.returnValue;
                    submitReplacementParts();
                }      
        });
    });                
    
    // $("#btnSave").click(function () {
        // _$div.jsonSubmit({
        //      procedure: "vehicle_repairs_upd" 
        //     ,parameters: {repair_date: _date, pms_type_id: _pmsType, vehicle_id: _vehicle, odo_reading: _odoReading, repair_amount: _amount, repair_location: _location, comment: _comment, status_id: _status}
        //     ,isSingleEntry: true
        //     ,onComplete: function (data) {
        //         if(data.isSuccess){
        //             _$div.find("input, textarea").val("");
                        // _$div.find("select").val(null).trigger('change');
                        // _$frm.removeClass('was-validated');
                        // $("#myModal").find("#msg").text("Data successfully saved.");
                    // $("#myModal").find("#msg").css("color","green"); 
                    // setTimeout(function(){
                    //     $("#myModal").modal('toggle');
                    // },1000);
        //         }else{
        //           $("#myModal").find("#msg").text("Something went wrong when saving the data.");
        //           $("#myModal").find("#msg").css("color","red");
        //         }
        //     }
        // }); 
    // });
      
    return _public;
    
    
    
})();       









       