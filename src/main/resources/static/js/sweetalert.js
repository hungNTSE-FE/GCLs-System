$('#insert1').on('click',function(e){
    e.preventDefault();
    var form = $(this).parents('form');
    swal.fire({
        position: "top-end",
        icon: "success",
        title: "Lưu thành công",
        showConfirmButton: false,
        timer: 3000,
    }, function(isConfirm){
        if (isConfirm) form.submit();
    });
});

$('#update1').on('click',function(e){
    e.preventDefault();
    var form = $(this).parents('form');
    swal.fire({
        position: "top-end",
        icon: "success",
        title: "Cập nhật thành công",
        showConfirmButton: false,
        timer: 3000,
    }, function(isConfirm){
        if (isConfirm) form.submit();
    });
});

$('#delete1').on('click',function(e){
    e.preventDefault();
    var form = $(this).parents('form');
    swal.fire({
        position: "top-end",
        icon: "success",
        title: "Xoá thành công",
        showConfirmButton: false,
        timer: 3000,
    }, function(isConfirm){
        if (isConfirm) form.submit();
    });
});

$('#cancelWork').on('click',function(e){
    e.preventDefault();
    var form = $(this).parents('form');
    swal.fire({
        position: "top-end",
        icon: "success",
        title: "Ngừng hoạt động thành công",
        showConfirmButton: false,
        timer: 3000,
    }, function(isConfirm){
        if (isConfirm) form.submit();
    });
});

$(document).ready(function() {
    const Toast = Swal.mixin({
        toast: true,
        position: 'top-end',
        showConfirmButton: false,
        timer: 3000,
        timerProgressBar: true,
        didOpen: (toast) => {
            toast.addEventListener('mouseenter', Swal.stopTimer)
            toast.addEventListener('mouseleave', Swal.resumeTimer)
        }
    })
    if (showtext === 'showAlert') {
        Toast.fire({
            icon: "success",
            title: "Xoá khách hàng thành công",
        });
    } else if (showtext === 'showAlertError') {
        Toast.fire({
            icon: 'error',
            title: "Xoá khách khàng thất bại",
        });
    } else if (showtext === 'showAlertImportSuccess') {
        Toast.fire({
            icon: 'success',
            title: 'Dữ liệu mới đã được thêm vào hệ thống',
        })
    } else if (showtext === 'showAlertShareSuccess') {
        Toast.fire({
            icon: 'success',
            title: 'Gửi dữ liệu thành công',
        })
    } else if (showtext === 'showAlertCreateCustomerSuccess') {
        Toast.fire({
            icon: 'success',
            title: "Thêm khách hàng thành công",
        });
    } else if (showtext === 'showAlertUpdateSuccessful') {
        Toast.fire({
            icon: 'success',
            title: "Cập nhật khách hàng thành công",
        });
    } else if (showtext === 'showAlertUpdateError') {
        Toast.fire({
            icon: 'error',
            title: "Cập nhật khách hàng thất bại",
        });
    } else if (showtext === 'showAlertSendEmail') {
        Toast.fire({
            icon: 'success',
            title: 'Gửi email thành công',
        })
    }
});
