package com.sporttourism.utils;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sporttourism.entities.Comment;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Type;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@DynamoDBTypeConverted(converter = CommentConverter.Converter.class)
public @interface CommentConverter {

  @AllArgsConstructor
  @NoArgsConstructor
  final class Converter implements DynamoDBTypeConverter<String, List<Comment>> {

    Gson gson = new Gson();

    @Override
    public String convert(final List<Comment> comment) {
      return comment.isEmpty() ? "[]" : gson.toJson(comment);
    }

    @Override
    public List<Comment> unconvert(final String commentString) {
      Type type = new TypeToken<List<Comment>>() {}.getType();

      return gson.fromJson(commentString, type);
    }
  }

}