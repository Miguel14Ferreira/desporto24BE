-- Curated sports classifications for official Cascais venue imports.
-- These are reference data and intentionally live in the database instead of Java code.

INSERT INTO venue_sport_classification (match_text, required_match_text, sports, priority)
SELECT v.match_text, v.required_match_text, v.sports, v.priority
FROM (
    VALUES
        ('adn atividades desportivas náuticas', NULL, 'Canoagem;Windsurf;Stand Up Paddle', 100),
        ('associação academia sulydance', NULL, 'Ballet;Dança Contemporânea;Hip Hop;Danças Latinas;Sevilhanas;Dance Fitness', 100),
        ('antigos alunos salesianos estoril', NULL, 'Judo;Futebol;Ténis;Ballet;Dança', 100),
        ('associação de moradores de atibá', NULL, 'Futsal;Atletismo;Zumba', 100),
        ('associação de moradores do bairro do junqueiro', NULL, 'Pickleball', 100),
        ('associação desportiva omael', NULL, 'Patinagem Artística', 100),
        ('academia ribeiro dos reis', NULL, 'Hidroginástica;Karaté;Natação;Pilates;Yoga;Zumba;Musculação;Cycling', 100),
        ('ahoki - hotel penha longa', NULL, 'Golfe', 100),
        ('associação desportiva da costa do sol', NULL, 'Futsal', 100),
        ('associação humanitária de bombeiros dos estoris', NULL, 'Natação;Karaté;Judo;Jiu Jitsu;Ginásio;Yoga;Fitdance', 100),
        ('associação familiar e desportiva da torre', NULL, 'Futebol', 100),
        ('associação grupo desportivo e recreativo do bairro da tojeira', NULL, 'Futsal;Patinagem Artística;Judo', 100),
        ('associação humanitária de bombeiros voluntários de cascais', 'complexo desportivo', 'Karaté;Judo;Pilates;Yoga;Alongamentos;Hidroginástica;Natação', 90),
        ('bombeiros voluntários dos estoris', NULL, 'Natação;Karaté;Judo;Jiu Jitsu;Ginásio;Yoga;Fitdance', 100),
        ('bombeiros da parede', NULL, 'Natação;Aqua Cross Training;Hidroginástica;Alongamentos;Ginástica Localizada;Kickboxing;Muay Thai;Pilates;Yoga', 100),
        ('socorros amadeu duarte', NULL, 'Natação;Aqua Cross Training;Hidroginástica;Alongamentos;Ginástica Localizada;Kickboxing;Muay Thai;Pilates;Yoga', 100),
        ('bombeiros voluntários de alcabideche', NULL, 'Musculação;Aikido;Brazilian Jiu Jitsu;Karaté;Krav Maga;Muay Thai;Kickboxing;Cardio Punch;Ginástica Localizada;Pilates;Yoga;Ballet', 100),
        ('associação nacional da espondilite anquilosante', NULL, 'Natação;Hidroginástica;Ginástica Sénior;Treino Cardiovascular;Pilates;Hidroterapia', 100),
        ('autódromo fernanda pires da silva', NULL, 'Automobilismo', 100),
        ('bela vista wellness club', NULL, 'Natação;Hidroginástica;Karaté;Judo;Ginásio', 100),
        ('bodyform gymnasium', NULL, 'Aeróbica;Musculação;Cardiofitness;Ginástica', 100),
        ('brahmi wellness', NULL, 'Yoga;Pilates;Meditação;Tai Chi;Chi Kung;Danças do Médio Oriente', 100),
        ('brahmi oriental', NULL, 'Yoga;Pilates;Meditação;Tai Chi;Chi Kung;Danças do Médio Oriente', 100),
        ('cam on let''s workout fitness studio', NULL, 'Zumba;Bum Bum Brasil', 100),
        ('camon let''s workout fitness studio', NULL, 'Zumba;Bum Bum Brasil', 100)
) AS v(match_text, required_match_text, sports, priority)
WHERE NOT EXISTS (
    SELECT 1
    FROM venue_sport_classification existing
    WHERE existing.match_text = v.match_text
      AND COALESCE(existing.required_match_text, '') = COALESCE(v.required_match_text, '')
);
