select
	e.employee_id employeeId,
	e.code code,
	e.username username,
	e.full_name fullName,
	e.email email,
	e.birthday birthday,
	e.gender gender,
    case
        when e.gender = 1 then 'Nam'
        when e.gender = 0 then 'Nữ'
        else 'null'
    end as genderStr,
	e.address
from
	employees e
where
	1 = 1