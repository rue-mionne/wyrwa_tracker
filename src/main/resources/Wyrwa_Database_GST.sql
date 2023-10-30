BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "EventTable" (
	"DayOfMonth"	INTEGER NOT NULL,
	"EventType"	TEXT NOT NULL,
	PRIMARY KEY("DayOfMonth")
);
CREATE TABLE IF NOT EXISTS "Characters" (
	"ID"	INTEGER NOT NULL,
	"Name"	TEXT NOT NULL,
	"EstCP"	INTEGER,
	"ClassID"	INTEGER NOT NULL,
	FOREIGN KEY("ClassID") REFERENCES "Classes"("ClassID") ON UPDATE NO ACTION ON DELETE NO ACTION,
	PRIMARY KEY("ID")
);
CREATE TABLE IF NOT EXISTS "Dungeons" (
	"ID"	INTEGER NOT NULL,
	"Name"	TEXT NOT NULL,
	"MinCP"	INTEGER NOT NULL,
	"OptimalCP"	INTEGER NOT NULL,
	"Region"	TEXT NOT NULL,
	PRIMARY KEY("ID")
);
CREATE TABLE IF NOT EXISTS "ItemData" (
	"ItemID"	INTEGER NOT NULL,
	"ItemName"	TEXT,
	"SalePrice"	INTEGER NOT NULL DEFAULT 0,
	"Icon"	BLOB,
	"Sellable"	INTEGER NOT NULL CHECK("Sellable" BETWEEN 0 AND 1),
	"Shareable"	INTEGER NOT NULL CHECK("Shareable" BETWEEN 0 AND 1),
	PRIMARY KEY("ItemID" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "Classes" (
	"ClassID"	INTEGER NOT NULL,
	"ClassName"	TEXT NOT NULL,
	"ShortClassName"	TEXT NOT NULL,
	"CharacterName"	TEXT NOT NULL,
	"DPSRating"	INTEGER,
	"CharacterIcon"	BLOB,
	PRIMARY KEY("ClassID")
);
CREATE TABLE IF NOT EXISTS "InventoryData" (
	"PairID"	INTEGER NOT NULL,
	"OwnerID"	INTEGER NOT NULL,
	"ItemID"	INTEGER NOT NULL,
	"Amount"	INTEGER NOT NULL,
	FOREIGN KEY("OwnerID") REFERENCES "Characters"("ID"),
	FOREIGN KEY("ItemID") REFERENCES "ItemData"("ItemID"),
	PRIMARY KEY("PairID")
);
CREATE TABLE IF NOT EXISTS "QuestCompletion" (
	"PairID"	INTEGER NOT NULL,
	"CharacterID"	INTEGER NOT NULL,
	"QuestID"	INTEGER NOT NULL,
	"Status"	TEXT NOT NULL CHECK("Status" = "INACTIVE" OR "Status" = "ACTIVE" OR "Status" = "COMPLETED"),
	"Progress"	INTEGER NOT NULL,
	"CompletionCount"	INTEGER NOT NULL,
	PRIMARY KEY("PairID"),
	FOREIGN KEY("CharacterID") REFERENCES "Characters"("ID") ON UPDATE NO ACTION ON DELETE NO ACTION,
	FOREIGN KEY("QuestID") REFERENCES "QuestData"("ID") ON UPDATE NO ACTION ON DELETE NO ACTION
);
CREATE TABLE IF NOT EXISTS "RewardData" (
	"RewardID"	INTEGER NOT NULL,
	"ItemID"	INTEGER NOT NULL,
	"QuestID"	INTEGER NOT NULL,
	"Amount"	REAL NOT NULL,
	FOREIGN KEY("QuestID") REFERENCES "QuestData"("ID"),
	FOREIGN KEY("ItemID") REFERENCES "ItemData"("ItemID"),
	PRIMARY KEY("RewardID")
);
CREATE TABLE IF NOT EXISTS "QuestData" (
	"ID"	INTEGER NOT NULL,
	"QuestName"	TEXT NOT NULL,
	"ProgressCount"	INTEGER NOT NULL,
	"CompletionType"	TEXT NOT NULL CHECK("CompletionType" = "REPEAT" OR "CompletionType" = "WEEKLY" OR "CompletionType" = "DAILY" OR "CompletionType" = "ONETIME"),
	"MaxCompletionCount"	INTEGER NOT NULL,
	"DungeonID"	INTEGER NOT NULL,
	"ED"	INTEGER NOT NULL,
	"EXP"	INTEGER NOT NULL,
	"EP"	INTEGER NOT NULL,
	"PreviousQuest"	INTEGER,
	"NextQuest"	INTEGER,
	"CharacterType"	TEXT NOT NULL CHECK("CharacterType" = "ALL" OR "CharacterType" = "LABY" OR "CharacterType" = "NOAH" OR "CharacterType" = "ELPACK"),
	FOREIGN KEY("DungeonID") REFERENCES "Dungeons"("ID") ON UPDATE NO ACTION ON DELETE NO ACTION,
	PRIMARY KEY("ID")
);
CREATE TABLE IF NOT EXISTS "Goals" (
	"GoalID"	INTEGER NOT NULL,
	"CharacterID"	INTEGER NOT NULL,
	"Priority"	REAL,
	PRIMARY KEY("GoalID"),
	FOREIGN KEY("CharacterID") REFERENCES "Characters"("ID")
);
CREATE TABLE IF NOT EXISTS "GoalItems" (
	"ID"	INTEGER NOT NULL,
	"GoalID"	INTEGER NOT NULL,
	"ItemID"	INTEGER NOT NULL,
	"Amount"	INTEGER NOT NULL,
	FOREIGN KEY("ItemID") REFERENCES "ItemData"("ItemID"),
	FOREIGN KEY("GoalID") REFERENCES "Goals"("GoalID"),
	PRIMARY KEY("ID")
);
INSERT INTO "EventTable" ("DayOfMonth","EventType") VALUES (1,'RM100');
INSERT INTO "EventTable" ("DayOfMonth","EventType") VALUES (2,'HEROIC2');
INSERT INTO "EventTable" ("DayOfMonth","EventType") VALUES (3,'FULLSD');
INSERT INTO "EventTable" ("DayOfMonth","EventType") VALUES (4,'ROSSO2');
INSERT INTO "EventTable" ("DayOfMonth","EventType") VALUES (5,'SD2');
INSERT INTO "EventTable" ("DayOfMonth","EventType") VALUES (6,'PROF50');
INSERT INTO "EventTable" ("DayOfMonth","EventType") VALUES (7,'RM100');
INSERT INTO "EventTable" ("DayOfMonth","EventType") VALUES (8,'HEROIC2');
INSERT INTO "EventTable" ("DayOfMonth","EventType") VALUES (9,'FULLSD');
INSERT INTO "EventTable" ("DayOfMonth","EventType") VALUES (10,'ROSSO2');
INSERT INTO "EventTable" ("DayOfMonth","EventType") VALUES (11,'SD2');
INSERT INTO "EventTable" ("DayOfMonth","EventType") VALUES (12,'PROF50');
INSERT INTO "EventTable" ("DayOfMonth","EventType") VALUES (13,'RM100');
INSERT INTO "EventTable" ("DayOfMonth","EventType") VALUES (14,'HEROIC2');
INSERT INTO "EventTable" ("DayOfMonth","EventType") VALUES (15,'FULLSD');
INSERT INTO "EventTable" ("DayOfMonth","EventType") VALUES (16,'ROSSO2');
INSERT INTO "EventTable" ("DayOfMonth","EventType") VALUES (17,'SD2');
INSERT INTO "EventTable" ("DayOfMonth","EventType") VALUES (18,'PROF50');
INSERT INTO "EventTable" ("DayOfMonth","EventType") VALUES (19,'RM100');
INSERT INTO "EventTable" ("DayOfMonth","EventType") VALUES (20,'HEROIC2');
INSERT INTO "EventTable" ("DayOfMonth","EventType") VALUES (21,'FULLSD');
INSERT INTO "EventTable" ("DayOfMonth","EventType") VALUES (22,'ROSSO2');
INSERT INTO "EventTable" ("DayOfMonth","EventType") VALUES (23,'SD2');
INSERT INTO "EventTable" ("DayOfMonth","EventType") VALUES (24,'PROF50');
INSERT INTO "EventTable" ("DayOfMonth","EventType") VALUES (25,'RM100');
INSERT INTO "EventTable" ("DayOfMonth","EventType") VALUES (26,'HEROIC2');
INSERT INTO "EventTable" ("DayOfMonth","EventType") VALUES (27,'FULLSD');
INSERT INTO "EventTable" ("DayOfMonth","EventType") VALUES (28,'ROSSO2');
INSERT INTO "EventTable" ("DayOfMonth","EventType") VALUES (29,'SD2');
INSERT INTO "EventTable" ("DayOfMonth","EventType") VALUES (30,'PROF50');
INSERT INTO "EventTable" ("DayOfMonth","EventType") VALUES (31,'RM100');
INSERT INTO "Characters" ("ID","Name","EstCP","ClassID") VALUES (1,'Ruruelam',1500000,28);
INSERT INTO "Characters" ("ID","Name","EstCP","ClassID") VALUES (2,'Rurue',900000,50);
INSERT INTO "Characters" ("ID","Name","EstCP","ClassID") VALUES (3,'Kyoan',750000,19);
INSERT INTO "Characters" ("ID","Name","EstCP","ClassID") VALUES (4,'Aspie',700000,51);
INSERT INTO "Characters" ("ID","Name","EstCP","ClassID") VALUES (5,'Pijen',700000,55);
INSERT INTO "Characters" ("ID","Name","EstCP","ClassID") VALUES (6,'Eunru',750000,25);
INSERT INTO "Characters" ("ID","Name","EstCP","ClassID") VALUES (7,'Supyeon',700000,54);
INSERT INTO "Characters" ("ID","Name","EstCP","ClassID") VALUES (8,'Apifera',450000,6);
INSERT INTO "Characters" ("ID","Name","EstCP","ClassID") VALUES (9,'Jinjeong',550000,53);
INSERT INTO "Characters" ("ID","Name","EstCP","ClassID") VALUES (10,'Mokke',500000,49);
INSERT INTO "Characters" ("ID","Name","EstCP","ClassID") VALUES (11,'MissEm',500000,52);
INSERT INTO "Characters" ("ID","Name","EstCP","ClassID") VALUES (12,'Senja',500000,11);
INSERT INTO "Characters" ("ID","Name","EstCP","ClassID") VALUES (13,'Eumhobu',450000,56);
INSERT INTO "Characters" ("ID","Name","EstCP","ClassID") VALUES (14,'Sushifa',500000,23);
INSERT INTO "Characters" ("ID","Name","EstCP","ClassID") VALUES (15,'Herrschia',NULL,18);
INSERT INTO "Characters" ("ID","Name","EstCP","ClassID") VALUES (16,'Samcheon',NULL,17);
INSERT INTO "Dungeons" ("ID","Name","MinCP","OptimalCP","Region") VALUES (1,'Hall Of El',50000,300000,'Elrianode');
INSERT INTO "Dungeons" ("ID","Name","MinCP","OptimalCP","Region") VALUES (2,'Water Dragon Sanctum',50000,300000,'Elrianode');
INSERT INTO "Dungeons" ("ID","Name","MinCP","OptimalCP","Region") VALUES (3,'Elrianode City',50000,300000,'Elrianode');
INSERT INTO "Dungeons" ("ID","Name","MinCP","OptimalCP","Region") VALUES (4,'Debrian Laboratory',100000,400000,'Elrianode');
INSERT INTO "Dungeons" ("ID","Name","MinCP","OptimalCP","Region") VALUES (5,'El Tower Defense (Story)',50000,300000,'Elrianode');
INSERT INTO "Dungeons" ("ID","Name","MinCP","OptimalCP","Region") VALUES (6,'El Tower Defense',150000,1000000,'Elrianode');
INSERT INTO "Dungeons" ("ID","Name","MinCP","OptimalCP","Region") VALUES (7,'Forgotten Elrian Sanctum',50000,300000,'Elrianode');
INSERT INTO "Dungeons" ("ID","Name","MinCP","OptimalCP","Region") VALUES (8,'Labyrinth of Ruin',130000,400000,'Varnimir');
INSERT INTO "Dungeons" ("ID","Name","MinCP","OptimalCP","Region") VALUES (9,'Guardian''s Forest',130000,400000,'Varnimir');
INSERT INTO "Dungeons" ("ID","Name","MinCP","OptimalCP","Region") VALUES (10,'Dark Elves'' Outpost',130000,400000,'Varnimir');
INSERT INTO "Dungeons" ("ID","Name","MinCP","OptimalCP","Region") VALUES (11,'Forsaken Spirits Asylum',200000,450000,'Varnimir');
INSERT INTO "Dungeons" ("ID","Name","MinCP","OptimalCP","Region") VALUES (12,'Rosso Raid (Story)',180000,450000,'Varnimir');
INSERT INTO "Dungeons" ("ID","Name","MinCP","OptimalCP","Region") VALUES (13,'Rosso Raid',350000,350000,'Varnimir');
INSERT INTO "Dungeons" ("ID","Name","MinCP","OptimalCP","Region") VALUES (14,'Shadow Vein',240000,550000,'Varnimir');
INSERT INTO "Dungeons" ("ID","Name","MinCP","OptimalCP","Region") VALUES (21,'Sea Of Ruin',270000,600000,'Rigomor');
INSERT INTO "Dungeons" ("ID","Name","MinCP","OptimalCP","Region") VALUES (22,'Abandoned Deep-Sea Tunnel',320000,700000,'Rigomor');
INSERT INTO "Dungeons" ("ID","Name","MinCP","OptimalCP","Region") VALUES (23,'Trosh''s Nest',550000,900000,'Rigomor');
INSERT INTO "Dungeons" ("ID","Name","MinCP","OptimalCP","Region") VALUES (24,'Abandoned Icerite Plant',550000,900000,'Rigomor');
INSERT INTO "Dungeons" ("ID","Name","MinCP","OptimalCP","Region") VALUES (25,'Titan''s Grotto',500000,500000,'Rigomor');
INSERT INTO "Dungeons" ("ID","Name","MinCP","OptimalCP","Region") VALUES (26,'Master Road',300000,1400000,'Master Road');
INSERT INTO "Dungeons" ("ID","Name","MinCP","OptimalCP","Region") VALUES (27,'Master Road (Hell)',600000,4000000,'Master Road');
INSERT INTO "Dungeons" ("ID","Name","MinCP","OptimalCP","Region") VALUES (28,'Heroic Dungeon',150000,1000000,'Special');
INSERT INTO "Dungeons" ("ID","Name","MinCP","OptimalCP","Region") VALUES (29,'Secret Dungeon',15000,100000,'Secret');
INSERT INTO "Classes" ("ClassID","ClassName","ShortClassName","CharacterName","DPSRating","CharacterIcon") VALUES (1,'Knight Emperor','KE','Elsword',NULL,NULL);
INSERT INTO "Classes" ("ClassID","ClassName","ShortClassName","CharacterName","DPSRating","CharacterIcon") VALUES (2,'Rune Master','RM','Elsword',NULL,NULL);
INSERT INTO "Classes" ("ClassID","ClassName","ShortClassName","CharacterName","DPSRating","CharacterIcon") VALUES (3,'Immortal','IM','Elsword',NULL,NULL);
INSERT INTO "Classes" ("ClassID","ClassName","ShortClassName","CharacterName","DPSRating","CharacterIcon") VALUES (4,'Genesis','GE','Elsword',NULL,NULL);
INSERT INTO "Classes" ("ClassID","ClassName","ShortClassName","CharacterName","DPSRating","CharacterIcon") VALUES (5,'Aether Sage','AeS','Aisha',NULL,NULL);
INSERT INTO "Classes" ("ClassID","ClassName","ShortClassName","CharacterName","DPSRating","CharacterIcon") VALUES (6,'Oz Sorcerer','OzS','Aisha',NULL,NULL);
INSERT INTO "Classes" ("ClassID","ClassName","ShortClassName","CharacterName","DPSRating","CharacterIcon") VALUES (7,'Metamorphy','MtM','Aisha',NULL,NULL);
INSERT INTO "Classes" ("ClassID","ClassName","ShortClassName","CharacterName","DPSRating","CharacterIcon") VALUES (8,'Lord Azoth','LA','Aisha',NULL,NULL);
INSERT INTO "Classes" ("ClassID","ClassName","ShortClassName","CharacterName","DPSRating","CharacterIcon") VALUES (9,'Anemos','AN','Rena',NULL,NULL);
INSERT INTO "Classes" ("ClassID","ClassName","ShortClassName","CharacterName","DPSRating","CharacterIcon") VALUES (10,'Daybreaker','DaB','Rena',NULL,NULL);
INSERT INTO "Classes" ("ClassID","ClassName","ShortClassName","CharacterName","DPSRating","CharacterIcon") VALUES (11,'Twilight','TW','Rena',NULL,NULL);
INSERT INTO "Classes" ("ClassID","ClassName","ShortClassName","CharacterName","DPSRating","CharacterIcon") VALUES (12,'Prophetess','PR','Rena',NULL,NULL);
INSERT INTO "Classes" ("ClassID","ClassName","ShortClassName","CharacterName","DPSRating","CharacterIcon") VALUES (13,'Furious Blade','FB','Raven',NULL,NULL);
INSERT INTO "Classes" ("ClassID","ClassName","ShortClassName","CharacterName","DPSRating","CharacterIcon") VALUES (14,'Rage Hearts','RH','Raven',NULL,NULL);
INSERT INTO "Classes" ("ClassID","ClassName","ShortClassName","CharacterName","DPSRating","CharacterIcon") VALUES (15,'Nova Imperator','NI','Raven',NULL,NULL);
INSERT INTO "Classes" ("ClassID","ClassName","ShortClassName","CharacterName","DPSRating","CharacterIcon") VALUES (16,'Revenant','RV','Raven',NULL,NULL);
INSERT INTO "Classes" ("ClassID","ClassName","ShortClassName","CharacterName","DPSRating","CharacterIcon") VALUES (17,'Code:Ultimate','CU','Eve',NULL,NULL);
INSERT INTO "Classes" ("ClassID","ClassName","ShortClassName","CharacterName","DPSRating","CharacterIcon") VALUES (18,'Code:Esencia','CE','Eve',NULL,NULL);
INSERT INTO "Classes" ("ClassID","ClassName","ShortClassName","CharacterName","DPSRating","CharacterIcon") VALUES (19,'Code:Sariel','CS','Eve',NULL,NULL);
INSERT INTO "Classes" ("ClassID","ClassName","ShortClassName","CharacterName","DPSRating","CharacterIcon") VALUES (20,'Code:Antithese','CA','Eve',NULL,NULL);
INSERT INTO "Classes" ("ClassID","ClassName","ShortClassName","CharacterName","DPSRating","CharacterIcon") VALUES (21,'Comet Crusader','CC','Chung',NULL,NULL);
INSERT INTO "Classes" ("ClassID","ClassName","ShortClassName","CharacterName","DPSRating","CharacterIcon") VALUES (22,'Fatal Phantom','FP','Chung',NULL,NULL);
INSERT INTO "Classes" ("ClassID","ClassName","ShortClassName","CharacterName","DPSRating","CharacterIcon") VALUES (23,'Centurion','CeT','Chung',NULL,NULL);
INSERT INTO "Classes" ("ClassID","ClassName","ShortClassName","CharacterName","DPSRating","CharacterIcon") VALUES (24,'Dius Aer','DA','Chung',NULL,NULL);
INSERT INTO "Classes" ("ClassID","ClassName","ShortClassName","CharacterName","DPSRating","CharacterIcon") VALUES (25,'Apsara','Aps','Ara',NULL,NULL);
INSERT INTO "Classes" ("ClassID","ClassName","ShortClassName","CharacterName","DPSRating","CharacterIcon") VALUES (26,'Devi','Devi','Ara',NULL,NULL);
INSERT INTO "Classes" ("ClassID","ClassName","ShortClassName","CharacterName","DPSRating","CharacterIcon") VALUES (27,'Shakti','SH','Ara',NULL,NULL);
INSERT INTO "Classes" ("ClassID","ClassName","ShortClassName","CharacterName","DPSRating","CharacterIcon") VALUES (28,'Surya','SU','Ara',NULL,NULL);
INSERT INTO "Classes" ("ClassID","ClassName","ShortClassName","CharacterName","DPSRating","CharacterIcon") VALUES (29,'Empire Sword','ES','Elesis',NULL,NULL);
INSERT INTO "Classes" ("ClassID","ClassName","ShortClassName","CharacterName","DPSRating","CharacterIcon") VALUES (30,'Flame Lord','FL','Elesis',NULL,NULL);
INSERT INTO "Classes" ("ClassID","ClassName","ShortClassName","CharacterName","DPSRating","CharacterIcon") VALUES (31,'Bloody Queen','BQ','Elesis',NULL,NULL);
INSERT INTO "Classes" ("ClassID","ClassName","ShortClassName","CharacterName","DPSRating","CharacterIcon") VALUES (32,'Adrestia','AD','Elesis',NULL,NULL);
INSERT INTO "Classes" ("ClassID","ClassName","ShortClassName","CharacterName","DPSRating","CharacterIcon") VALUES (33,'Doom Bringer','DB','Add',NULL,NULL);
INSERT INTO "Classes" ("ClassID","ClassName","ShortClassName","CharacterName","DPSRating","CharacterIcon") VALUES (34,'Dominator','DoM','Add',NULL,NULL);
INSERT INTO "Classes" ("ClassID","ClassName","ShortClassName","CharacterName","DPSRating","CharacterIcon") VALUES (35,'Mad Paradox','MP','Add',NULL,NULL);
INSERT INTO "Classes" ("ClassID","ClassName","ShortClassName","CharacterName","DPSRating","CharacterIcon") VALUES (36,'Overmind','OM','Add',NULL,NULL);
INSERT INTO "Classes" ("ClassID","ClassName","ShortClassName","CharacterName","DPSRating","CharacterIcon") VALUES (37,'Catastrophe','CaT','Lu/Ciel',NULL,NULL);
INSERT INTO "Classes" ("ClassID","ClassName","ShortClassName","CharacterName","DPSRating","CharacterIcon") VALUES (38,'Innocent','IN','Lu/Ciel',NULL,NULL);
INSERT INTO "Classes" ("ClassID","ClassName","ShortClassName","CharacterName","DPSRating","CharacterIcon") VALUES (39,'Diangelion','DiA','Lu/Ciel',NULL,NULL);
INSERT INTO "Classes" ("ClassID","ClassName","ShortClassName","CharacterName","DPSRating","CharacterIcon") VALUES (40,'Demersion','DeM','Lu/Ciel',NULL,NULL);
INSERT INTO "Classes" ("ClassID","ClassName","ShortClassName","CharacterName","DPSRating","CharacterIcon") VALUES (41,'Tempest Burster','TB','Rose',NULL,NULL);
INSERT INTO "Classes" ("ClassID","ClassName","ShortClassName","CharacterName","DPSRating","CharacterIcon") VALUES (42,'Black Massacre','BM','Rose',NULL,NULL);
INSERT INTO "Classes" ("ClassID","ClassName","ShortClassName","CharacterName","DPSRating","CharacterIcon") VALUES (43,'Minerva','MN','Rose',NULL,NULL);
INSERT INTO "Classes" ("ClassID","ClassName","ShortClassName","CharacterName","DPSRating","CharacterIcon") VALUES (44,'Prime Operator','PO','Rose',NULL,NULL);
INSERT INTO "Classes" ("ClassID","ClassName","ShortClassName","CharacterName","DPSRating","CharacterIcon") VALUES (45,'Richter','RT','Ain',NULL,NULL);
INSERT INTO "Classes" ("ClassID","ClassName","ShortClassName","CharacterName","DPSRating","CharacterIcon") VALUES (46,'Bluhen','BL','Ain',NULL,NULL);
INSERT INTO "Classes" ("ClassID","ClassName","ShortClassName","CharacterName","DPSRating","CharacterIcon") VALUES (47,'Herrscher','HR','Ain',NULL,NULL);
INSERT INTO "Classes" ("ClassID","ClassName","ShortClassName","CharacterName","DPSRating","CharacterIcon") VALUES (48,'Opferung','OP','Ain',NULL,NULL);
INSERT INTO "Classes" ("ClassID","ClassName","ShortClassName","CharacterName","DPSRating","CharacterIcon") VALUES (49,'Eternity Winner','EtW','Laby',NULL,NULL);
INSERT INTO "Classes" ("ClassID","ClassName","ShortClassName","CharacterName","DPSRating","CharacterIcon") VALUES (50,'Radiant Soul','RaS','Laby',NULL,NULL);
INSERT INTO "Classes" ("ClassID","ClassName","ShortClassName","CharacterName","DPSRating","CharacterIcon") VALUES (51,'Nisha Labyrinth','NL','Laby',NULL,NULL);
INSERT INTO "Classes" ("ClassID","ClassName","ShortClassName","CharacterName","DPSRating","CharacterIcon") VALUES (52,'Twins Picaro','TP','Laby',NULL,NULL);
INSERT INTO "Classes" ("ClassID","ClassName","ShortClassName","CharacterName","DPSRating","CharacterIcon") VALUES (53,'Liberator','LiB','Noah',NULL,NULL);
INSERT INTO "Classes" ("ClassID","ClassName","ShortClassName","CharacterName","DPSRating","CharacterIcon") VALUES (54,'Celestia','CL','Noah',NULL,NULL);
INSERT INTO "Classes" ("ClassID","ClassName","ShortClassName","CharacterName","DPSRating","CharacterIcon") VALUES (55,'Nyx Pieta','NP','Noah',NULL,NULL);
INSERT INTO "Classes" ("ClassID","ClassName","ShortClassName","CharacterName","DPSRating","CharacterIcon") VALUES (56,'Morpheus','MO','Noah',NULL,NULL);
CREATE UNIQUE INDEX IF NOT EXISTS "ClassID" ON "Characters" (
	"ClassID"
);
CREATE UNIQUE INDEX IF NOT EXISTS "CharacterID" ON "QuestCompletion" (
	"CharacterID",
	"QuestID"
);
CREATE INDEX IF NOT EXISTS "QuestID" ON "QuestCompletion" (
	"QuestID"
);
CREATE INDEX IF NOT EXISTS "DungeonID" ON "QuestData" (
	"DungeonID"
);
COMMIT;
