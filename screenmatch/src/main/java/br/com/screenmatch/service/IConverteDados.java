package br.com.screenmatch.service;

public interface IConverteDados  {
    <T> T obeterDados (String json, Class<T> classe);
}
