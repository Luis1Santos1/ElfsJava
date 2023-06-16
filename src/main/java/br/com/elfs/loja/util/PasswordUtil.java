package br.com.elfs.loja.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    public static String criptografarSenha(String senha) {
        // Gere um salt aleatório
        String salt = BCrypt.gensalt();

        // Criptografe a senha usando o salt gerado
        String senhaCriptografada = BCrypt.hashpw(senha, salt);

        return senhaCriptografada;
    }

    public static boolean verificarSenha(String senha, String senhaCriptografada) {
        // Verifique se a senha fornecida corresponde à senha criptografada
        return BCrypt.checkpw(senha, senhaCriptografada);
    }
}
