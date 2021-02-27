UPDATE candidates
SET contact_number = '+381621127650', date_of_birth = '1998-04-25 00:00:00.000000',
    email = 'radovic.milorad1998@gmail.com', full_name = 'Milorad Radovic'
WHERE id = 1;

INSERT INTO candidate_skill (candidate_id, skill_id) values (1, 2);
INSERT INTO candidate_skill (candidate_id, skill_id) values (1, 10);
INSERT INTO candidate_skill (candidate_id, skill_id) values (1, 8);
INSERT INTO candidate_skill (candidate_id, skill_id) values (1, 7);
INSERT INTO candidate_skill (candidate_id, skill_id) values (1, 1);