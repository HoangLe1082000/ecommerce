$('document').ready(function (){
    $('table #editButton').on('click', function (event){
            event.preventDefault();
            var href = $(this).attr('href');
            console.log(href)
            $.get(href, function (category, status){
                $('#idCategory').val(category.id);
                $('#nameCategory').val(category.name);
            });

            $('#editModal').modal();
    })


})