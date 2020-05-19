   var refuel = (function(){
    var  _pub = {}
        ,gCurrDate = new Date() +''
        ,gFilterDate = ""
        ,gFilterGasStationId = ""
    ;
    
    zsi.ready = function(){
        $(".page-title").html("Refuel");
        $(".panel-container").css("min-height", $(window).height() - 160);
        
        
        var _$grid = $("#gridRefuel");
        $("#filterDate").datepicker({
             autoclose : true
            ,todayHighlight: true
            ,startDate: new Date()
        }).on("hide", function(e) {
            gFilterDate = this.value;
            var _$row = _$grid.find(".zRow");
            _$row.each(function(){
                var _refuelId = $(this).find("[name='refuel_id']").val();
                if( _refuelId==="" ){
                    $(this).find("[name='doc_date']").val(gFilterDate);
                }    
            });
        }).datepicker("setDate", "0");
        
        $("#filterGasStation").dataBind({
            sqlCode      : "G215" //gas_stations_sel
            ,text         : "gas_station_name"
            ,value        : "gas_station_id"
            ,onChange     : function(d){
                var _info = d.data[d.index - 1];
                gas_station_id = isUD(_info) ? "" : _info.gas_station_id; 
                gFilterGasStationId = gas_station_id;
                var _$row = _$grid.find(".zRow");
                _$row.each(function(){
                    var _refuelId = $(this).find("[name='refuel_id']").val();
                    if( _refuelId==="" ){
                        $(this).find("[name='gas_station_id']").val(gFilterGasStationId);
                    }    
                });
            }
        });
        
        displayRecords();
        // validations();
        // $('#pao_id').select2({placeholder: "SELECT PAO",allowClear: true});
        // $('#driver_id').select2({placeholder: "SELECT DRIVER",allowClear: true});
        // $('#vehicle_id').select2({placeholder: "SELECT VEHICLE",allowClear: true});
        // $('#gas_station').select2({placeholder: "SELECT GAS STATION",allowClear: true});
        // //$("#client_phone_no").inputmask({"mask": "(99) 9999 - 9999"});
        // $("#doc_date").datepicker({todayHighlight:true}).datepicker("setDate",new Date());
        // $("#pms_type_id").dataBind({
        //     sqlCode      : "D235" //dd_pms_type_sel
        //   ,text         : "pms_desc"
        //   ,value        : "pms_type_id"
        //   ,onChange     : function(d){
        //       var _info           = d.data[d.index - 1]
        //           pms_type_id     = isUD(_info) ? "" : _info.pms_type_id;
               
        //   }
        // });
        // $("#vehicle_id").dataBind({
        //     sqlCode      : "D231" //dd_vehicle_sel
        //   ,text         : "plate_no"
        //   ,value        : "vehicle_id"
        //   ,onChange     : function(d){
        //       var _info           = d.data[d.index - 1]
        //           vehicle_id     = isUD(_info) ? "" : _info.vehicle_id;
               
        //   }
        // });
        
        // $("#driver_id").dataBind({
        //     sqlCode      : "D227" //drivers_sel
        //   ,text         : "full_name"
        //   ,value        : "user_id"
        //   ,onChange     : function(d){
        //       var _info           = d.data[d.index - 1]
        //           _driver_id         = isUD(_info) ? "" : _info.user_id;
        //         //gDriverId = _driver_id;
        //   }
        // });
        
        // $("#pao_id").dataBind({
        //     sqlCode      : "P228" //pao_sel
        //   ,text         : "full_name"
        //   ,value        : "user_id"
        //   ,onChange     : function(d){
        //       var _info           = d.data[d.index - 1]
        //           _driver_id         = isUD(_info) ? "" : _info.user_id;
        //         //gDriverId = _driver_id;
        //   }
        // });
        
        // $("#gas_station").dataBind({
        //      sqlCode    : "G215" // gas_station_sel
        //     ,text   : "gas_station_name"
        //     ,value  : "gas_station_id"
        // });
        
    };
    
    function displayRecords(){
        zsi.getData({
             sqlCode    : "R267" //refuel_sel
            ,parameters : {doc_date: gFilterDate, gas_station_id: gFilterGasStationId}
            ,onComplete : function(d) {
                var _rows = d.rows;
                var _total = _rows.reduce(function (accumulator, currentValue) {
                    return parseFloat(accumulator) + parseFloat(currentValue.refuel_amount);
                }, 0);  
                
                var _rowTotal = {
                    refuel_id : ""
                    ,doc_date : ""
                    ,doc_no : ""
                    ,vehicle_id : ""
                    ,driver_id : ""
                    ,pao_id : ""
                    ,odo_reading : ""
                    ,gas_station_id : ""
                    ,no_liters : ""
                    ,unit_price : "Total Amount"
                    ,refuel_amount : _total
                }; 
                _rows.push(_rowTotal);
                
                $("#gridRefuel").dataBind({
                     rows : _rows
                    ,height : $("#divReplacementParts").closest(".panel-container").height()
                    ,blankRowsLimit : 10
                    ,dataRows : [
                        {text: "Document Date", width: 100
                            ,onRender : function(d){ 
                                return app.bs({type: "hidden", name: "refuel_id", value: app.svn(d,"refuel_id")})
                                    +  app.bs({type: "hidden", name: "is_edited"})  
                                    + app.bs({type: "input", name: "doc_date", value: app.svn(d,"doc_date", (gFilterDate ? gFilterDate : gCurrDate)).toShortDate() });
                            }
                        }
                        ,{text: "Document #", type: "input", name: "doc_no", width: 100, style: "text-align:center"}
                        ,{text: "Vehicle", type: "select", name: "vehicle_id", width: 120, style: "text-align:left"}
                        ,{text: "Driver", type: "select", name: "driver_id", width: 130, style: "text-align:left"}
                        ,{text: "PAO", type: "select", name: "pao_id", width: 130, style: "text-align:left"}
                        ,{text: "ODO Reading", width: 100
                            ,onRender : function(d){ 
                                return app.bs({type: "input", name: "odo_reading", value: app.svn(d,"odo_reading").toCommaSeparatedNo(), style: "text-align:center"});
                            }
                        }
                        ,{text: "Gas Station", width: 120 
                            ,onRender : function(d){ 
                                return app.bs({type: "select", name: "gas_station_id", value: app.svn(d,"gas_station_id", (gFilterGasStationId ? gFilterGasStationId : "")), style: "text-align:left"});
                            }
                        }
                        ,{text: "No. of Liter(s)", width: 120
                            ,onRender : function(d){ 
                                return app.bs({type: "input", name: "no_liters", value: app.svn(d,"no_liters").toCommaSeparatedDecimal(), style: "text-align:center"});
                            }
                        }
                        ,{text: "Unit Price", width: 100
                            ,onRender : function(d){ 
                                var _unitPrice = app.svn(d,"unit_price");
                                if(_unitPrice==="Total Amount"){
                                    return "<b class='d-block px-1 text-white text-right'>"+ _unitPrice +"</b>";
                                }else return app.bs({type: "input", name: "unit_price", value: _unitPrice.toCommaSeparatedDecimal(), style: "text-align:right"});
                            }
                        }
                        ,{text: "Amount", width: 100
                            ,onRender : function(d){ 
                                var _unitPrice = app.svn(d,"unit_price");
                                var _refuelAmt = app.svn(d,"refuel_amount").toCommaSeparatedDecimal();
                                if(_unitPrice==="Total Amount"){
                                    return "<b class='d-block px-1 text-white text-right'>"+ _refuelAmt +"</b>";
                                }else return app.bs({type: "input", name: "refuel_amount", value: _refuelAmt, style: "text-align:right"});
                            }
                        }
                    ]
                    ,onComplete : function(o){
                        var _$grid = this;
                        _$grid.find(".zRow:nth-child("+ o.data.length +")").addClass("zTotal position-absolute");
                        
                        _$grid.find("[name='doc_date']").datepicker({
                             autoclose : true
                            ,todayHighlight: true
                            ,startDate: new Date()
                        });
                
                        _$grid.find("[name='vehicle_id']").dataBind({
                            sqlCode      : "D231" //dd_vehicle_sel
                           ,text         : "plate_no"
                           ,value        : "vehicle_id"
                           ,onChange     : function(d){}
                        });
                        
                        _$grid.find("[name='driver_id']").dataBind({
                            sqlCode      : "D227" //drivers_sel
                           ,text         : "full_name"
                           ,value        : "user_id"
                           ,onChange     : function(d){}
                        });
                        
                        _$grid.find("[name='pao_id']").dataBind({
                            sqlCode      : "P228" //pao_sel
                           ,text         : "full_name"
                           ,value        : "user_id"
                           ,onChange     : function(d){}
                        });
                        
                        _$grid.find("[name='gas_station_id']").dataBind({
                            sqlCode      : "G215" //gas_stations_sel
                           ,text         : "gas_station_name"
                           ,value        : "gas_station_id"
                           ,onChange     : function(d){}
                        });
                        
                        _$grid.find("[name='no_liters'],[name='unit_price']").focusout(function(){
                            var _$row = $(this).closest(".zRow");
                            var _$amount = _$row.find("[name='refuel_amount']")
                                ,_$noLiters = _$row.find("[name='no_liters']")
                                ,_$unitPrice = _$row.find("[name='unit_price']")
                                ,_noLiters = _$noLiters.val().replace(/,/g, "")
                                ,_unitPrice = _$unitPrice.val().replace(/,/g, "")
                                ,_amount = "";
                                
                                if(_noLiters!=="" && _unitPrice!==""){
                                    _amount = parseFloat(_noLiters).toFixed(2) * parseFloat(_unitPrice).toFixed(2);
                                    _$amount.val(_amount.toCommaSeparatedDecimal());
                                }else{
                                    _$amount.val("");
                                }
                        });
                        
                        _$grid.find("[name='seq_no'],[name='refuel_amount']").attr("readonly",true);
                        _$grid.find("[name='odo_reading']").addClass("integer");
                        _$grid.find("[name='no_liters'],[name='unit_price'],[name='refuel_amount']").addClass("numeric");
                        zsi.initInputTypesAndFormats();
                        appendFooter(_$grid);
                    }
                });
            }
        });
    }
    
    function appendFooter(grid){
        var _$zTotal = grid.find(".zTotal")
            ,_$clone = _$zTotal.clone();
            
        _$zTotal.remove();
        _$clone.find("input, select").remove();
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
      
    $("#btnFilter").click(function(){
        gFilterDate = $("#filterDate").val();
        gFilterGasStationId = $("#filterGasStation").val();
        displayRecords();
    });
    
    $("#btnSave").click(function () {
        var _$grid = $("#gridRefuel");
        _$grid.find("[name='odo_reading'],[name='no_liters'],[name='unit_price'],[name='refuel_amount']").each(function(){
            this.value = this.value.replace(/,/g, "");
        });
        _$grid.jsonSubmit({
             procedure: "refuel_upd"
            ,onComplete: function (data) {
                if(data.isSuccess){
                   if(data.isSuccess===true) zsi.form.showAlert("alert");
                   displayRecords();
                //   $("form").removeClass('was-validated');
                //   $("#formRefuel").find("input").val("");
                //   $("#formRefuel").find("textarea").val("");
                //   $("#formRefuel").find("select").val(null).trigger('change');
                //   $("#myModal").find("#msg").text("Data successfully saved.");
                //   $("#myModal").find("#msg").css("color","green");
                //   setTimeout(function(){
                //       $("#myModal").modal('toggle');
                //       $("#doc_date").datepicker({todayHighlight:true}).datepicker("setDate",new Date());
                //       modalTxt();
                //   },1000);
                }else{
                //   $("#myModal").find("#msg").text("Something went wrong when saving the data.");
                //   $("#myModal").find("#msg").css("color","red");
                }
            }
        }); 
    });
    
    function modalTxt(){
        setTimeout(function(){
           $("#myModal").find("#msg").text("Are you sure you want to save this data?");
           $("#myModal").find("#msg").css("color","#000");
        },1000);
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
  
    return _pub;
})();               