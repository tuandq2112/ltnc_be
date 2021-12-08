drop function if exists thong_ke_so_luong_dang_ky(integer);

create
or replace function thong_ke_so_luong_dang_ky(nam_khao_sat integer)
    returns table
            (
                id       double precision,
                month    double precision ,
                year     double precision,
                total    bigint
            )

as
$body$
begin

return query
select t.month                                      as id,
       t.month                                      as month,
       t.year                                       as year,
       t.total                                      as total
from (select extract (month from r.create_at) as month, extract (year from r.create_at) as year, count (r.id) as total
    from register r
    group by extract (year from r.create_at), extract (month from r.create_at)) as t
where t.year = nam_khao_sat;

end ;
$body$
language plpgsql stable
                     cost 100
;

