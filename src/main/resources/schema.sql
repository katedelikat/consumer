CREATE TABLE IF NOT EXISTS tasks (
           "id" SERIAL,
           "creation_time" timestamptz NOT NULL,
           "modification_time" timestamptz NULL,
           "task_text" VARCHAR NOT NULL,
           "task_status" VARCHAR NULL,
           "consumer_id" VARCHAR NULL,
           PRIMARY KEY ("id")
);


CREATE TABLE IF NOT EXISTS consumers (
           "id" VARCHAR PRIMARY KEY,
           "bulk_size" INTEGER NOT NULL
);