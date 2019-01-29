$(document).ready(function()
{
	PopulateDeliveryForm();

	//print
	$("#PrintDoc").on("click",function()
	{
		alert("Click");
		window.print();
	});
	//print
});


function PopulateDeliveryForm()
{
	for (var i = 0; i > 12; i++)
	{
	}
	//array container for the computer rentals
	var ComList = [];
	var SampleParts = ["PROCESSOR: INTEL CORE I5-4460 3.2 GHZ","MOTHERBOARD: ASUS H81M-C","MEMORY: 8GB PC1600 DDR3", "HDD: 500 SEAGATE SATA","LAN CARD: B- IN","ATX CASE W/ 700W POWER SUPPLY"]; 
	//sample data
	ComList.push(new ComputerRentals("15RENTAL-480",SampleParts));
	ComList.push(new ComputerRentals("15RENTAL-481",SampleParts));
	ComList.push(new ComputerRentals("15RENTAL-482",SampleParts));
	ComList.push(new ComputerRentals("15RENTAL-483",SampleParts));
	ComList.push(new ComputerRentals("15RENTAL-484",SampleParts));
	ComList.push(new ComputerRentals("15RENTAL-485",SampleParts));
	
	console.log(ComList);
	//sample data
	//Appending Computer Rentals
	$.each(ComList,function(index, value)
	{
		var nextIndex = index++;
		console.log("nextIndex "+nextIndex);
		if (nextIndex%4==0)
		{
			console.log(index+" This is the 4th item");
		}

		var x     = "";
			x    += '<div class="PartsContainer col-lg-6">';
			x    +=	'<span class="rentalNumber"><strong>'+value.rentalNumber+'</strong></span>';
			x    +=	'<br>';
			$.each(value.Parts,function(index,value)
			{
				x+='<span>'+value+'</span><br>';
			});
			x    += '</div>';

			$("#itemList").append(x);
	});

	var AccessoriesList = [];
	AccessoriesList.push(new Accesories("352","A4TECH", "KEYBOARD"));
	AccessoriesList.push(new Accesories("434","A4TECH", "KEYBOARD"));
	AccessoriesList.push(new Accesories("280","A4TECH", "KEYBOARD"));
	AccessoriesList.push(new Accesories("444","A4TECH", "KEYBOARD"));
	AccessoriesList.push(new Accesories("429","A4TECH", "USB OPTICAL MOUSE"));
	AccessoriesList.push(new Accesories("429","A4TECH", "USB OPTICAL MOUSE"));

	if (AccessoriesList.length!=0)
	{
		var y = "";
		    y+='<h3 style="text-align:center">Accessories</h3>'
		    y+='<div id="partsList" class="row"></div>';
		    $("#itemList").after(y);
	}
	var AccessoriesList = chunkArray(AccessoriesList,4);
	console.log(AccessoriesList);
	$.each(AccessoriesList,function(index, xchuck)
	{
		var x     = "";
			x    += '<div class="PartsContainer col-lg-6">';
			$.each(xchuck,function(index,xAccessories)
			{
				x    +=	'<span class="rentalNumber"><strong>'+xAccessories.RentalNumber+"</strong> "+xAccessories.brand+" "+xAccessories.name+'</span><br><br>';
			});	

			x    += '</div>';
		$("#partsList").append(x);
	});		
}

function chunkArray(myArray, chunk_size)
{
    var index       = 0;
    var arrayLength = myArray.length;
    var tempArray   = [];
    
    for (index = 0; index < arrayLength; index += chunk_size)
    {
        myChunk = myArray.slice(index, index+chunk_size);
        tempArray.push(myChunk);
    }

    return tempArray;
}

//computer rental object
function ComputerRentals(RentalNumber,Parts = [])
{
	this.rentalNumber = RentalNumber;
	this.Parts        = Parts;
}

function Accesories(RentalNumber,brand,name)
{
	this.RentalNumber = RentalNumber;
	this.brand        = brand;
	this.name         = name;
}

function printDiv() 
{
	// var restorepage = document.body.innerHTML;
	// var printcontent = document.getElementById("rr-form").innerHTML;
	// document.body.innerHTML = printcontent;
	// window.print();
	var mywindow = window.open('', 'PRINT', 'height=400,width=600');

    mywindow.document.write('<html><head><title>' + document.title  + '</title>');
    mywindow.document.write('</head><body >');
    mywindow.document.write('<h1>' + document.title  + '</h1>');
    mywindow.document.write(document.getElementById("rr-form").innerHTML);
    mywindow.document.write('</body></html>');

    mywindow.document.close(); // necessary for IE >= 10
    mywindow.focus(); // necessary for IE >= 10*/

    mywindow.print();
    mywindow.close();

    return true;
}