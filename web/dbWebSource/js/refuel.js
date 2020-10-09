   var refuel = (function(){
    var  _pub = {}
        ,gCurrDate = new Date() +''
        ,gFilterDate = ""
        ,gFilterGasStationId = ""
        ,gTotal = 0.00
    ;
    
    zsi.ready = function(){
        $(".page-title").html("Fuel Expenses");
        $(".panel-container").css("min-height", $(window).height() - 160);
        
        
        var _$grid = $("#gridRefuel");
        $("#filterDate").datepicker({
             autoclose : true
            ,todayHighlight: true
            ,endDate: new Date()
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
            sqlCode      : "G215"
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
    };
    
    function displayRecords(){
         var totality = 0.00;
         var cb = app.bs({name:"cbFilter1",type:"checkbox"});
        zsi.getData({
             sqlCode    : "R267"
            ,parameters : {doc_date: gFilterDate, gas_station_id: gFilterGasStationId}
            ,onComplete : function(d) {
                var _rows = d.rows;
                for(var i=0; i < _rows.length;i++ ){
                    var _info = _rows[i];
                    totality    +=_info.refuel_amount; 
                }
                
                console.log("gTotal",gTotal);
                
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
                    ,refuel_amount : 0.00
                }; 
                _rows.push(_rowTotal);
                
                $("#gridRefuel").dataBind({
                    rows : _rows
                    ,height : $(window).height() - 288
                    ,blankRowsLimit : 10
                    ,dataRows : [
                        {text:cb        ,width:25              ,style : "text-align:left"
                            ,onRender  :  function(d){ return (d !==null ? app.bs({name:"cb"        ,type:"checkbox"}) : "" );
                            }
                        }
                        ,{text: "Document Date", width: 100
                            ,onRender : function(d){ 
                                return app.bs({type: "hidden"   ,name: "refuel_id"      ,value: app.svn(d,"refuel_id")})
                                    +  app.bs({type: "hidden"   ,name: "is_edited"})  
                                    +  app.bs({type: "input"    ,name: "doc_date"       ,value: app.svn(d,"doc_date", (gFilterDate ? gFilterDate : gCurrDate)).toShortDate() });
                            }
                        }
                        ,{text: "Reference No"          ,type: "input"      ,name: "doc_no"         ,width: 100, style: "text-align:center"}
                        ,{text: "Vehicle"               ,type: "select"     ,name: "vehicle_id"     ,width: 120, style: "text-align:left"}
                        ,{text: "Driver"                ,type: "select"     ,name: "driver_id"      ,width: 130, style: "text-align:left"}
                        ,{text: "PAO"                   ,type: "select"     ,name: "pao_id"         ,width: 130, style: "text-align:left"}
                        ,{text: "ODO Reading", width: 100
                            ,onRender : function(d){ 
                                return app.bs({type: "input"        ,name: "odo_reading"        ,value: app.svn(d,"odo_reading").toCommaSeparatedNo()       ,style: "text-align:center"});
                            }
                        }
                        ,{text: "Gas Station", width: 120 
                            ,onRender : function(d){ 
                                return app.bs({type: "select"       ,name: "gas_station_id"     ,value: app.svn(d,"gas_station_id", (gFilterGasStationId ? gFilterGasStationId : ""))       ,style: "text-align:left"});
                            }
                        }
                        ,{text: "No. of Liter(s)", width: 100
                            ,onRender : function(d){ 
                                return app.bs({type: "input"        ,name: "no_liters"          ,value: app.svn(d,"no_liters")      ,style: "text-align:center"});
                            }
                        }
                        ,{text: "Unit Price", width: 100
                            ,onRender : function(d){ 
                                var _unitPrice = app.svn(d,"unit_price");
                                if(_unitPrice==="Total Amount"){
                                    return "<b class='d-block px-1 text-white text-right'>"+ _unitPrice +"</b>";
                                }else return app.bs({type: "input"      ,name: "unit_price"         ,value: _unitPrice.toCommaSeparatedDecimal()        ,style: "text-align:right"});
                            }
                        }
                        
                        ,{text: "Amount", width: 100
                            ,onRender : function(d){ 
                                var _unitPrice = app.svn(d,"unit_price");
                                var _refuelAmt = app.svn(d,"refuel_amount").toCommaSeparatedDecimal();
                                if(_unitPrice==="Total Amount"){
                                    return "<b id='total' class='d-block px-1 text-white text-right'>"+ totality.toCommaSeparatedDecimal() +"</b>";
                                }else return app.bs({type: "input"      ,name: "refuel_amount"      ,value: _refuelAmt      ,style: "text-align:right"})
                                          +  app.bs({type: "hidden"     ,name: "is_posted"});
                            }
                        }
                    ]
                    ,onComplete : function(o){
                        $("[name='cbFilter1']").setCheckEvent("#gridRefuel input[name='cb']");
                        $(".zRow:contains('Total Amount')").addClass("zTotal");
                        $(".zRow:contains('Total Amount')").find("[name='vehicle_id'],[name='driver_id'],[name='pao_id'],[name='gas_station_id'],[name='cb'],[name='odo_reading'],[name='no_liters'],[name='doc_date'],[name='doc_no']").remove();
                        var _$grid = this;
                        this.find("[name='unit_price']").maskMoney();
                        _$grid.find("[name='doc_date']").datepicker({
                             autoclose : true
                            ,todayHighlight: true
                            ,endDate: new Date()
                        });
                
                        _$grid.find("[name='vehicle_id']").dataBind({
                            sqlCode      : "D272"
                           ,parameters   : {client_id:app.userInfo.company_id}
                           ,text         : "vehicle_plate_no"
                           ,value        : "vehicle_id"
                        });
                        
                        _$grid.find("[name='driver_id']").dataBind({
                            sqlCode      : "D270" 
                           ,parameters   : {client_id:app.userInfo.company_id}
                           ,text         : "emp_lfm_name"
                           ,value        : "id"
                        });
                        
                        _$grid.find("[name='pao_id']").dataBind({
                            sqlCode      : "D271" 
                           ,parameters   : {client_id:app.userInfo.company_id}
                           ,text         : "emp_lfm_name"
                           ,value        : "id"
                        });
                        
                        _$grid.find("[name='gas_station_id']").dataBind({
                            sqlCode      : "G215"
                           ,text         : "gas_station_name"
                           ,value        : "gas_station_id"
                           ,onChange     : function(d){}
                        });
                        
                        function total(){
                            var _$lastRow = $(".zRow:contains('Total Amount')").find("#total");
                            var _totalCost = $(".zRow:not(:contains('Total Amount'))").find("[name='refuel_amount']");
                            var _data = [];
                            var _totality = 0.00;
                            
                            _totalCost.each(function(){
                                if(this.value) _data.push(this.value.replace(/,/g, ""));
                            });
                            
                            for (var i = 0; i < _data.length; i++){
                               _totality += parseFloat(_data[i]);
                            }
                           
                            gTotal = _totality.toCommaSeparatedDecimal();
                           _$lastRow.text(_totality.toCommaSeparatedDecimal());
                        }
                        
                        _$grid.find("[name='no_liters'],[name='unit_price']").on("keyup change",function(){
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
                                    total();
                                }else{
                                    _$amount.val("");
                                }
                        });
                        
                        _$grid.find("[name='seq_no'],[name='refuel_amount']").attr("readonly",true);
                        _$grid.find("[name='odo_reading']").addClass("integer");
                        _$grid.find("[name='unit_price'],[name='refuel_amount']").addClass("numeric");
                        zsi.initInputTypesAndFormats();
                        setFooterFreezed(_$grid);
                    }
                });
            }
        });
    }
    
    function setFooterFreezed(zGridId){ 
        var _zRows = $(zGridId).find(".zGridPanel.right .zRows");
        var _tableRight   = _zRows.find("#table");
        var _zRowsHeight =   _zRows.height() - 38;
        var _everyZrowsHeight = $(".zRow:not(:contains('Total Amount'))");
        var _arr = [];
        var _height = 0;
        var _zTotal = _tableRight.find(".zRow:contains('Total Amount')");
        
        _everyZrowsHeight.each(function(){
            if(this.clientHeight) _arr.push(this.clientHeight);
        });
        
        for (var i = 0; i < _arr.length; i++){
           _height += _arr[i];
        }
        
        _zTotal.css({"top": _zRowsHeight});
        
        if(_zRows.find(".zRow").length == 1){
            _zTotal.addClass("hide");
        }else{
            if(_tableRight.height() > _zRowsHeight){
                _tableRight.parent().scroll(function() {
                   _zTotal.css({"top":_zRowsHeight - ( _tableRight.offset().top - _zRows.offset().top) });
                });
            }else{
                _zTotal.css({"top": _height});
            }
        }
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
    
    $("#btnResetVal").click(function(){
        $("#filterGasStation").val("");
        $("#filterDate").datepicker(new Date());
        gFilterDate = "";
        gFilterGasStationId = "";
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
                }
            }
        }); 
    });
   

    $("#btnPost").click(function () {
           var _$grid = $("#gridRefuel");
           _$grid.find("[name='odo_reading'],[name='no_liters'],[name='unit_price'],[name='refuel_amount']").each(function(){
               this.value = this.value.replace(/,/g, "");
           });
    
            _$grid.find('input:checked').each(function() {
                   $(this).closest(".zRow").find("[name='is_posted']").val("Y");
            });   
           _$grid.jsonSubmit({
                procedure: "refuel_upd"
               ,onComplete: function (data) {
                   if(data.isSuccess){
                      if(data.isSuccess===true) zsi.form.showAlert("alert");
                      displayRecords();
                 
                   }else{
                 
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