delete
from tokens;

insert into tokens(expired, revoker, user_id, token, token_type)
values (false, false, 1,
        'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtYWtzaW1AbWFpbC5jb20iLCJpYXQiOjE2OTAzMDg1MzQsImV4cCI6MTY5MDM5NDkzNH0.VM0ACNXe-Xl8_o_NnosQWPGEvczrHzr4Nv7cOTKNW8c',
        'BEARER');