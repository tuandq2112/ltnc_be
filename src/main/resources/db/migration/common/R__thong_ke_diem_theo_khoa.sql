drop function if exists thong_ke_diem_theo_khoa(bigint);

create
    or replace function thong_ke_diem_theo_khoa(id_khoa bigint)
    returns table
            (
                id      bigint,
                name    text,
                subject text,
                ids     text,
                pass    text,
                total   text

            )

as
$body$
begin

    return query
        select u.id                             as id,
               u.full_name::text                as name,
               string_agg(s.name, ',')          as subject,
               string_agg(r.id::text, ',')      as ids,
               string_agg(r.is_pass::text, ',') as pass,
               string_agg(r.total::text, ',')   as total
        from results r
                 left join users u on r.student_id = u.id
                 left join subject s on r.subject_id = s.id
        where r.course_id = id_khoa
        group by u.id, u.full_name;

end ;
$body$
    language plpgsql stable
                     cost 100
;
