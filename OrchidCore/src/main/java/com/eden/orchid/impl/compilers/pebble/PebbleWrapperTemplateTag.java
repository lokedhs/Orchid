package com.eden.orchid.impl.compilers.pebble;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.compilers.TemplateTag;
import com.eden.orchid.impl.compilers.pebble.tag.BaseTagParser;
import com.eden.orchid.impl.compilers.pebble.tag.ContentTagParser;
import com.eden.orchid.impl.compilers.pebble.tag.SimpleTagParser;
import com.eden.orchid.impl.compilers.pebble.tag.TabbedTagParser;
import com.mitchellbosecke.pebble.error.ParserException;
import com.mitchellbosecke.pebble.extension.NodeVisitor;
import com.mitchellbosecke.pebble.lexer.Token;
import com.mitchellbosecke.pebble.node.AbstractRenderableNode;
import com.mitchellbosecke.pebble.node.RenderableNode;
import com.mitchellbosecke.pebble.parser.Parser;
import com.mitchellbosecke.pebble.template.EvaluationContextImpl;
import com.mitchellbosecke.pebble.template.PebbleTemplateImpl;
import com.mitchellbosecke.pebble.tokenParser.TokenParser;

import javax.inject.Provider;
import java.io.IOException;
import java.io.Writer;

public final class PebbleWrapperTemplateTag implements TokenParser {
    private final Provider<OrchidContext> contextProvider;
    private final String name;
    private final TemplateTag.Type type;
    private final String[] tagParameters;
    private final Class<? extends TemplateTag> tagClass;
    private final String[] tabParameters;
    private final Class<? extends TemplateTag.Tab> tabClass;

    public PebbleWrapperTemplateTag(Provider<OrchidContext> contextProvider, String name, TemplateTag.Type type, String[] tagParameters, Class<? extends TemplateTag> tagClass, String[] tabParameters, Class<? extends TemplateTag.Tab> tabClass) {
        this.contextProvider = contextProvider;
        this.name = name.toLowerCase();
        this.type = type;
        this.tagParameters = tagParameters;
        this.tagClass = tagClass;
        this.tabParameters = tabParameters;
        this.tabClass = tabClass;
    }

    @Override
    public String getTag() {
        return name;
    }

    @Override
    public RenderableNode parse(Token token, Parser parser) throws ParserException {
        try {
            return doParse(token, parser);
        }
        catch (Exception e) {
            throw new ParserException(e, "error parsing '" + name + "'", token.getLineNumber(), parser.getStream().getFilename());
        }
    }

    private RenderableNode doParse(Token token, Parser parser) {
        switch (type) {
            case Simple:
                return new SimpleTagParser(contextProvider, name, tagParameters, tagClass).parse(token, parser);

            case Content:
                return new ContentTagParser(contextProvider, name, tagParameters, tagClass).parse(token, parser);

            case Tabbed:
                return new TabbedTagParser(contextProvider, name, tagParameters, tagClass, tabParameters, tabClass).parse(token, parser);

            default:

                throw new IllegalArgumentException("Tag type must be a valid type.");
        }
    }


    public static class TemplateTagNode extends AbstractRenderableNode {
        private final BaseTagParser parser;

        public TemplateTagNode(int lineNumber, BaseTagParser parser) {
            super(lineNumber);
            this.parser = parser;
        }

        @Override
        public void render(PebbleTemplateImpl self, Writer writer, EvaluationContextImpl context) throws IOException {
            this.parser.render(self, writer, context);
        }

        @Override
        public void accept(NodeVisitor visitor) {
            visitor.visit(this);
        }
    }

    public Provider<OrchidContext> getContextProvider() {
        return this.contextProvider;
    }

    public String getName() {
        return this.name;
    }

    public TemplateTag.Type getType() {
        return this.type;
    }

    public String[] getTagParameters() {
        return this.tagParameters;
    }

    public Class<? extends TemplateTag> getTagClass() {
        return this.tagClass;
    }

    public String[] getTabParameters() {
        return this.tabParameters;
    }

    public Class<? extends TemplateTag.Tab> getTabClass() {
        return this.tabClass;
    }
}
