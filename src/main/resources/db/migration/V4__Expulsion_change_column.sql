ALTER TABLE expulsion CHANGE image_key fileKey text;
ALTER TABLE expulsion CHANGE expulsion_image_status status text;
ALTER TABLE expulsion CHANGE expulsion_image_name file_category text;
ALTER TABLE expulsion CHANGE hard_delete_date soft_delete_date timestamp;

ALTER TABLE expulsion ADD COLUMN domain_id bigint not null;
ALTER TABLE expulsion ADD COLUMN domain_type text not null;
ALTER TABLE expulsion ADD COLUMN file_type text not null;
ALTER TABLE expulsion ADD COLUMN hard_delete_date timestamp null;