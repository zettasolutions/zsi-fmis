 var repair = (function(){
    var  _public = {}
        ,gRepairId = "" 
    ;
    zsi.ready = function(){
        $(".page-title").html("Repair");
        $(".panel-container").css("min-height", $(window).height() - 160);   
        //validations();
        dispalyReplacementParts();
        markRepairMandatory();
        selects();
    };
    function dispalyReplacementParts(){
        var _getData = function(cb){
            var _rows = [];
            if(gRepairId!==""){
                zsi.getData({
                     sqlCode    : "P249" //part_replacements_sel
                    ,parameters : {repair_id: gRepairId}
                    ,onComplete : function(d) {
                        _rows = d.rows;
                        cb(_rows);
                    }
                });
            }else cb(_rows);
        };
        
        _getData(function(_rows){
            console.log(_rows);
            var _total = _rows.reduce(function (accumulator, currentValue) {
                return parseFloat(accumulator) + parseFloat(currentValue.total_cost);
            }, 0);  
            var _seqNo = -1;
            var _rowTotal = {
                replacement_id : ""
                ,pms_id : ""
                ,repair_id : ""
                ,seq_no : ""
                ,part_id : ""
                ,part_qty : ""
                ,unit_id : ""
                ,unit_cost : "Overall Cost"
                ,total_cost : _total
                ,is_replacement : ""
                ,is_bnew : ""
            }; 
            _rows.push(_rowTotal);
            
            $("#gridReplacementParts").dataBind({
                 rows: _rows
                ,height : $("#divReplacementParts").closest(".panel-container").height()
                ,blankRowsLimit : 10
                ,dataRows : [
                    {text: "Item No.", width: 60
                        ,onRender : function(d){ 
                            _seqNo++;
                            return app.bs({type: "hidden", name: "replacement_id", value: app.svn(d,"replacement_id")})
                                +  app.bs({type: "hidden", name: "is_edited"})  
                                +  app.bs({type: "hidden", name: "pms_id"})  
                                +  app.bs({type: "hidden", name: "repair_id", value: gRepairId})  
                                +  app.bs({type: "input",  name: "seq_no", value: (app.svn(d,"seq_no") ? app.svn(d,"seq_no") : (_seqNo > 0) ? _seqNo : ""), style: "text-align:center"}); 
                        }
                    }
                    ,{text: "Part", type: "select", name: "part_id", width: 130, style: "text-align:left"}
                    ,{text: "Part Qty", width: 120
                        ,onRender : function(d){ 
                            return app.bs({type: "input",  name: "part_qty", value: app.svn(d,"part_qty").toCommaSeparatedDecimal(), style: "text-align:center"});
                        }
                    }
                    ,{text: "Unit", type: "select", name: "unit_id", width: 130, style: "text-align:left"}
                    ,{text: "Unit Cost", width: 120
                        ,onRender : function(d){ 
                            var _unitCost = app.svn(d,"unit_cost");
                            if(_unitCost==="Overall Cost"){
                                return "<b class='d-block px-1 text-white text-right'>"+ _unitCost +"</b>";
                            }else return app.bs({type: "input",  name: "unit_cost", value: _unitCost.toCommaSeparatedDecimal(), style: "text-align:center"});
                        }
                    }
                    ,{text: "Total Cost", width: 120
                        ,onRender : function(d){
                            var _unitCost = app.svn(d,"unit_cost");
                            var _totalCost = app.svn(d,"total_cost").toCommaSeparatedDecimal();
                            if(_unitCost==="Overall Cost"){
                                return "<b class='d-block px-1 text-white text-right'>"+ _totalCost +"</b>";
                            }else return app.bs({type: "input",  name: "total_cost", value: _totalCost, style: "text-align:center"});
                        }
                    }
                    ,{text: "Replaced (Yes/No)", type: "yesno", name: "is_replacement", width: 120, style: "text-align:center", defaultValue:"N"}
                    ,{text: "New (Yes/No)", type: "yesno", name: "is_bnew", width: 100, style: "text-align:center", defaultValue:"N"}
                ]
                ,onComplete : function(o){
                    markReplacementPartsMandatory();

                    var _$grid = this;
                    _$grid.find(".zRow:nth-child("+ o.data.length +")").addClass("zTotal position-absolute");
                    
                    _$grid.find("[name='part_id']").dataBind({
                        sqlCode      : "D256" //dd_parts_sel
                       ,text         : "part_desc"
                       ,value        : "part_id"
                       ,onChange     : function(d){
                        //   var _info = d.data[d.index - 1];
                        //   part_id = isUD(_info) ? "" : _info.part_id; 
                       }
                    });
                    
                    _$grid.find("[name='unit_id']").dataBind({
                        sqlCode      : "D257" //dd_units_sel
                       ,text         : "unit_name"
                       ,value        : "unit_id"
                       ,onChange     : function(d){
                        //   var _info = d.data[d.index - 1];
                        //   unit_id = isUD(_info) ? "" : _info.unit_id; 
                       }
                    });
                    
                    _$grid.find("[name='part_qty'],[name='unit_cost']").focusout(function(){
                        var _$row = $(this).closest(".zRow");
                        var _$totalCost = _$row.find("[name='total_cost']")
                            ,_$partQty = _$row.find("[name='part_qty']")
                            ,_$unitCost = _$row.find("[name='unit_cost']")
                            ,_partQty = _$partQty.val().replace(/,/g, "")
                            ,_unitCost = _$unitCost.val().replace(/,/g, "")
                            ,_totalCost = "";
                            
                            if(_partQty!=="" && _unitCost!==""){
                                _totalCost = parseFloat(_partQty).toFixed(2) * parseFloat(_unitCost).toFixed(2);
                                _$totalCost.val(_totalCost.toCommaSeparatedDecimal());
                            }else{
                                _$totalCost.val("");
                            }
                    });
                    
                    _$grid.find("[name='seq_no']").attr("readonly",true);
                    _$grid.find("[name='total_cost']").attr("disabled",true);
                    _$grid.find("[name='part_qty'],[name='unit_cost']").addClass("numeric");
                    zsi.initInputTypesAndFormats();
                    appendFooter(_$grid);
                }
            });
        });
    }
    
    function appendFooter(grid){
        var _$zTotal = grid.find(".zTotal")
            ,_$zHeader = grid.find(".zHeaders")
            ,_$clone = _$zTotal.clone();
            
        _$zTotal.remove();
        _$clone.find("input, select").remove();
        _$clone.css("width", _$zHeader.width() - 17);
        grid.find(".zRows > #table").append(_$clone);
        setFooterFreezed(grid);
    }
    
    function setFooterFreezed(grid){
        var _$zRows = grid.find(".zRows");
        var _$tbl   = _$zRows.find("#table");
        var _zRowsHeight =   _$zRows.height();
        var _$zTotal = _$tbl.find(".zTotal");
        
        if(_$zRows.width() < _$tbl.width()){
            _zRowsHeight -= 40;
        }else _zRowsHeight -= 23;
        
        _$zTotal.css({"top": _zRowsHeight});
        _$zTotal.prev().css({"margin-bottom":23 }); 

        if(_$zRows.find(".zRow").length == 1){
            _$zTotal.addClass("hide");
        }else{
            if(_$tbl.height() > _zRowsHeight){
                _$tbl.parent().scroll(function() {
                   _$zTotal.css({"top":_zRowsHeight - ( _$tbl.offset().top - _$zRows.offset().top) });
                });
            }else{
                _$zTotal.css({"position":"unset"});
                _$zTotal.prev().css({"margin-bottom":0 });
            }
        }
        
        $(window).unbind().resize(function(){
            setFooterFreezed(grid);
            _$tbl.parent().scroll();
        });
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
        $("#repair_amount").attr("readonly", true);
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
    // function setInputFilter(textbox, inputFilter) {
    //   ["input", "keydown"].forEach(function(event) {
    //     textbox.addEventListener(event, function() {
    //       if (inputFilter(this.value)) {
    //         this.oldValue = this.value;
    //         this.oldSelectionStart = this.selectionStart;
    //         this.oldSelectionEnd = this.selectionEnd;
    //       } else if (this.hasOwnProperty("oldValue")) {
    //         this.value = this.oldValue;
    //         this.setSelectionRange(this.oldSelectionStart, this.oldSelectionEnd);
    //       } else {
    //         this.value = "";
    //       }
    //     });
    //   });
    // } 
    // setInputFilter(document.getElementById("odo_reading"), function(value) {
    //   return /^-?\d*$/.test(value);  
    // }); 
    
    function markRepairMandatory(){
        $("#divVehicleRepair").markMandatory({       
            "groupNames":[
                {
                     "names" : ["repair_date","vehicle_id","odo_reading"]
                } 
            ]      
            ,"groupTitles":[ 
                 {"titles" : ["Repair Date","Vehicle","ODO Reading"]}
            ]
        }); 
    }
    
    function markReplacementPartsMandatory(){
        $("#gridReplacementParts").markMandatory({       
            "groupNames":[
                {
                     "names" : ["part_id","part_qty","unit_id","unit_cost"]
                    ,"type":"M"
                } 
            ]      
            ,"groupTitles":[ 
                 {"titles" : ["Part","Part Qty.","Unit","Unit Cost"]}
            ]
        }); 
    }
    
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
            ,_odoReading = $("#odo_reading").val().replace(/,/g, "")
            //,_amount = $("#repair_amount").val().replace(/,/g, "")
            ,_serviceAmt = $("#service_amount").val().replace(/,/g, "")
            ,_location = $("#repair_location").val()
            ,_comment = $("#comment").val()
            ,_status = $("#status_id").val();
            
        if( _$div.checkMandatory()!==true) return false;
        if( $("#gridReplacementParts").checkMandatory()!==true) return false;
            
        $.post(app.procURL + "vehicle_repairs_upd @repair_id='"+ gRepairId +"',@repair_date='"+ _date +"',@pms_type_id="+ _pmsType +",@vehicle_id="+ _vehicle +",@odo_reading="+ _odoReading
                            +",@repair_location='" +_location +"',@comment='"+ _comment +"',@service_amount="+ _serviceAmt +",@status_id='"+ _status +"'"
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









         