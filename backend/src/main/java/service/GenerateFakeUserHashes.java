package service;

public class GenerateFakeUserHashes {
    public static void main(String[] args) {
        String[] passwords = {
            "DesertStorm1!",
            "SunDevil2025!",
            "CactusBloom7!",
            "TucsonRocks9!",
            "PhoenixHeat4!",
            "WildcatStrong8!",
            "SaguaroSky3!",
            "GrandCanyon6!",
            "CopperState2!",
            "Roadrunner5!",
            "ValleySun11!",
            "MesaMountain12!",
            "RedRock13!",
            "DustDevil14!",
            "HeatWave15!",
            "CanyonTrail16!",
            "SunsetBlaze17!",
            "HorizonGlow18!",
            "SolarFlare19!",
            "DesertNight20!"
        };

        for (int i = 0; i < passwords.length; i++) {
            String salt = PasswordHasher.generateSalt();
            String hash = PasswordHasher.hash(passwords[i], salt);
            System.out.printf(
                "(%d, 'user%02d', '%s', '%s', 'AreaHere', 'token_%d'),%n",
                i + 1, i + 1, hash, salt, i + 1
            );
        }
    }
}