CREATE TABLE public.cache_example
(
  ce_id      INT          PRIMARY KEY,
  ce_value   VARCHAR(100) NOT NULL
);

INSERT INTO public.cache_example (ce_id, ce_value) VALUES (4, 'This is the number 4');